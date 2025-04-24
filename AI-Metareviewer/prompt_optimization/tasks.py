import requests
import json
import concurrent.futures
import os
from abc import ABC, abstractmethod
from typing import List, Dict, Callable
from tqdm import tqdm
import pandas as pd
from sklearn.metrics import accuracy_score, f1_score, classification_report
from custom_utils import toString

class DataProcessor(ABC):
    def __init__(self, data_dir, max_threads=1):
        self.data_dir = data_dir
        self.max_threads = max_threads

    @abstractmethod
    def get_train_examples(self):
        pass

    @abstractmethod
    def get_test_examples(self):
        pass

    @abstractmethod
    def evaluate(self, predictor, test_exs):
        pass

    @abstractmethod
    def stringify_prediction(self, pred):
        pass




def process_example(ex, predictor, prompt):
    pred = predictor.inference(ex, prompt)
    return ex, pred


class ClassificationTask(DataProcessor):

    def run_evaluate(self, predictor, prompt, test_exs, n=100):
        labels = []
        preds = []
        texts = []
        
        # Create checkpoint directory if it doesn't exist
        checkpoint_dir = os.path.join(self.data_dir, 'checkpoints')
        os.makedirs(checkpoint_dir, exist_ok=True)
        checkpoint_file = os.path.join(checkpoint_dir, 'evaluation_progress.json')
        
        # Load previous progress if exists
        if os.path.exists(checkpoint_file):
            with open(checkpoint_file, 'r') as f:
                progress = json.load(f)
                start_idx = progress['last_processed_idx'] + 1
                labels = progress['labels']
                preds = progress['preds']
        else:
            start_idx = 0
        
        batch_size = 50
        for batch_start in range(start_idx, min(n, len(test_exs)), batch_size):
            batch_end = min(batch_start + batch_size, n)
            batch_exs = test_exs[batch_start:batch_end]
            
            try:
                with concurrent.futures.ProcessPoolExecutor(max_workers=self.max_threads) as executor:
                    futures = [executor.submit(process_example, ex, predictor, prompt) for ex in batch_exs]
                    for future in tqdm(concurrent.futures.as_completed(futures), 
                                     total=len(futures), 
                                     desc=f'Processing batch {batch_start//batch_size + 1}'):
                        ex, pred = future.result()
                        texts.append(ex['text'])
                        labels.append(ex['label'])
                        preds.append(pred)
                
                # Save progress after each successful batch (only labels and preds)
                progress = {
                    'last_processed_idx': batch_end - 1,
                    'labels': labels,
                    'preds': preds
                }
                with open(checkpoint_file, 'w') as f:
                    json.dump(progress, f)
                    
            except (concurrent.futures.process.BrokenProcessPool, requests.exceptions.SSLError) as e:
                print(f"Error processing batch {batch_start//batch_size + 1}: {str(e)}")
                # Remove checkpoint file to indicate incomplete processing
                if os.path.exists(checkpoint_file):
                    os.remove(checkpoint_file)
                raise e

        accuracy = accuracy_score(labels, preds)
        f1 = f1_score(labels, preds, average='micro')
        return f1, texts, labels, preds

    def evaluate(self, predictor, prompt, test_exs, n=100):
        while True:
            try:
                f1, texts, labels, preds = self.run_evaluate(predictor, prompt, test_exs, n=n)
                break
            except (concurrent.futures.process.BrokenProcessPool, requests.exceptions.SSLError):
                pass
        return f1, texts, labels, preds


class BinaryClassificationTask(ClassificationTask):
    categories = ['No', 'Yes']

    def stringify_prediction(self, pred):
        return BinaryClassificationTask.categories[pred]


class EthosBinaryTask(BinaryClassificationTask):
    categories = ['No', 'Yes']

    def get_train_examples(self):
        df = pd.read_csv(self.data_dir + '/ethos_ishate_binary_shuf.csv', sep=';', header=None)
        df = df[(df[1] <= 0) | (df[1] >= 0.7)]
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x['index'], 'text': x[0], 'label': 1 if x[1] > 0.4 else 0} for x in exs[200:]]
        print(exs)
        return exs
    
    def get_test_examples(self):
        df = pd.read_csv(self.data_dir + '/ethos_ishate_binary_shuf.csv', sep=';', header=None)
        df = df[(df[1] <= 0) | (df[1] >= 0.7)]
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x['index'], 'text': x[0], 'label': 1 if x[1] > 0.4 else 0} for x in exs[:200]]
        return exs


class JailbreakBinaryTask(BinaryClassificationTask):
    categories = ['No', 'Yes']

    def get_train_examples(self):
        exs = []
        for i, l in enumerate(open(self.data_dir + '/train.tsv')):
            convo, label = l.strip().split('\t')
            label = int(label)
            text = ' '.join([x['text'].strip() for x in json.loads(convo) if x['role'] == 'user'])
            exs.append({'id': i, 'text': text, 'label': label})
        return exs
    
    def get_test_examples(self):
        exs = []
        for i, l in enumerate(open(self.data_dir + '/test.tsv')):
            convo, label = l.strip().split('\t')
            label = int(label)
            text = ' '.join([x['text'].strip() for x in json.loads(convo) if x['role'] == 'user'])
            exs.append({'id': i, 'text': text, 'label': label})
        return exs


class DefaultHFBinaryTask(BinaryClassificationTask):
    categories = ['No', 'Yes']

    def get_train_examples(self):
        exs = []
        for i, row in enumerate(open(self.data_dir + '/train.jsonl')):
            row = json.loads(row.strip())
            exs.append({'id': f'train-{i}', 'label': row['label'], 'text': row['text']})
        return exs
    
    def get_test_examples(self):
        exs = []
        for i, row in enumerate(open(self.data_dir + '/test.jsonl')):
            row = json.loads(row.strip())
            exs.append({'id': f'test-{i}', 'label': row['label'], 'text': row['text']})
        return exs


class MetareviewerBinaryTask(BinaryClassificationTask):
    categories = ['No', 'Yes']

    def get_train_examples(self):
        df = pd.read_csv(self.data_dir + 'metareviewer_data_train_200.csv', sep=';')
        # df = pd.read_csv(self.data_dir + '00metareviewer_data_balanced.csv', sep=';')
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x['id'], 'text': x['text'], 'label': int(x['label'])} for x in exs]
        return exs
    
    def get_test_examples(self):
        df = pd.read_csv(self.data_dir + 'metareviewer_data_test_200.csv', sep=';')
        # df = pd.read_csv(self.data_dir + '00metareviewer_data_balanced.csv', sep=';')
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x['id'], 'text': x['text'], 'label': int(x['label'])} for x in exs]
        return exs