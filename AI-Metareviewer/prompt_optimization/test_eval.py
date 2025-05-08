import tasks
import predictors
from tqdm import tqdm
import json
import ast
import scorers
import evaluators
import re
import argparse
from sklearn.metrics import f1_score

if __name__ == '__main__':

    # Round - from prior results
    candidates = ["Given the following reviews, determine if the paper being reviewed would be accepted at an academic conference."]

    args = {"n_test_exs": 200, "temperature": 0.0, "out": "results/initial_prompts.out", "model": "gpt-4o-mini"}

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)
    test_exs = task.get_test_examples('data/metareviewer_data_test_200.csv')
            
    metrics = []
    for candidate in candidates:
        ids_lables_totals = {}
        for ex in test_exs:
            ids_lables_totals[ex['id']] = [ex['label']]  

        trials_ids_preds = {}
        for j in range(1):   
            sub_ids = []
            sub_preds = []
            sub_labels = []
            for i in range(args["n_test_exs"] // 50):
                ids, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs[i * 50 : (i + 1) * 50], 50)
                sub_ids.extend(ids)
                sub_preds.extend(preds)
                sub_labels.extend(labels)
            f1 = f1_score(sub_labels, sub_preds, average='micro')
            print(f"cnt_preds: {len(sub_preds)}")

            with open(args["out"], 'a') as outf:  
                outf.write(f'Step {j}: {f1}\n') 

            # for id, pred in zip(sub_ids, sub_preds):
            #     if id not in trials_ids_preds:
            #         trials_ids_preds[id] = [pred]
            #     else:
            #         trials_ids_preds[id].append(pred)

        # for id, preds in trials_ids_preds.items():
        #     if len(preds) == 1:
        #         ids_lables_totals[id].append(max(set(preds), key=preds.count))
        #     else:
        #         del trials_ids_preds[id]
        #         del ids_lables_totals[id]

        # pairs = ids_lables_totals.values() 
        # true_labels = [pair[0] for pair in pairs]
        # pred_labels = [pair[1] for pair in pairs]
        # print(f"len(true_labels): {len(true_labels)}")
        # print(f"len(pred_labels): {len(pred_labels)}")     
        # f1 = f1_score(true_labels, pred_labels, average='micro')
        # metrics.append(f1)
        # with open(args["out"], 'a') as outf:  
        #     outf.write(f'voted_f1: {metrics}\n') 
