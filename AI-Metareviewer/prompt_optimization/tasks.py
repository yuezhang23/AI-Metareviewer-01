import requests
import json
import concurrent.futures
from abc import ABC, abstractmethod
from typing import List, Dict, Callable
from tqdm import tqdm
import pandas as pd
from sklearn.metrics import accuracy_score, f1_score, classification_report
from custom_utils import toString
import psycopg
from psycopg.rows import dict_row
import time

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



def process_example(ex, predictor, prompt, model):
    pred = predictor.inference(ex, prompt)
    return ex, pred



class ClassificationTask(DataProcessor):

    def run_evaluate(self, predictor, prompt, test_exs, n, batch_size, max_retries=3):
        ids = []
        labels = []
        preds = []
        texts = []
        
        total_examples = min(n, len(test_exs))
        
        if hasattr(predictor, 'batch_inference'):
            total_batches = (total_examples + batch_size - 1) // batch_size
            with tqdm(total=total_examples, desc='Evaluating examples') as pbar:
                for i in range(0, total_examples, batch_size):
                    batch_exs = test_exs[i : i + batch_size]
                    retry_count = 0
                    
                    while retry_count < max_retries:
                        try:
                            batch_preds = predictor.batch_inference(batch_exs, prompt, model='gpt-4.1-nano')
                            for ex, pred in zip(batch_exs, batch_preds):
                                ids.append(ex['id'])
                                texts.append(ex['text'])
                                labels.append(ex['label'])
                                preds.append(pred)
                            pbar.update(len(batch_exs))
                            break
                        except (requests.exceptions.SSLError,
                               requests.exceptions.RequestException) as e:
                            retry_count += 1
                            if retry_count == max_retries:
                                raise Exception(f"Failed after {max_retries} retries: {str(e)}")
                            time.sleep(2 ** retry_count)  # Exponential backoff
                            continue
        else:
            with tqdm(total=total_examples, desc='Evaluating examples') as pbar:
                with concurrent.futures.ProcessPoolExecutor(max_workers=self.max_threads) as executor:
                    futures = [executor.submit(process_example, ex, predictor, prompt, model='gpt-4.1-nano') for ex in test_exs[:total_examples]]
                    for future in concurrent.futures.as_completed(futures):
                        try:
                            ex, pred = future.result()
                            ids.append(ex['id'])
                            texts.append(ex['text'])
                            labels.append(ex['label'])
                            preds.append(pred)
                            pbar.update(1)
                        except Exception as e:
                            print(f"Error processing example: {str(e)}")
                            continue

        accuracy = accuracy_score(labels, preds)
        f1 = f1_score(labels, preds, average='micro')
        return ids, f1, texts, labels, preds

    def evaluate(self, predictor, prompt, test_exs, n=400, batch_size=4):
        try:
            ids, f1, texts, labels, preds = self.run_evaluate(predictor, prompt, test_exs, n=n, batch_size=batch_size)
            return ids, f1, texts, labels, preds
        except Exception as e:
            print(f"Evaluation failed: {str(e)}")
            raise


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

    def get_train_examples(self, path):
        df = pd.read_csv(path, sep=';', header=None)
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x[0], 'text': x[1], 'label': int(x[2])} for x in exs]
        return exs
    
    def get_test_examples(self, path):
        df = pd.read_csv(path, sep=';', header=None)
        exs = df.reset_index().to_dict('records')
        exs = [{'id': x[0], 'text': x[1], 'label': int(x[2])} for x in exs]
        return exs
