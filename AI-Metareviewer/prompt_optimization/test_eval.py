import tasks
import predictors
from tqdm import tqdm
import json
import ast
import scorers
import evaluators
import re
import argparse
import random
from sklearn.metrics import f1_score

if __name__ == '__main__':

    # Round - from prior results
    # candidates = ["Given the following reviews, determine if the paper being reviewed would be accepted at an academic conference."]

    candidates = ["# Task\nExamine the evaluations provided for the academic paper and determine its overall quality by pinpointing key strengths and weaknesses. In your assessment, evaluate the soundness of the methodology, the clarity of the presentation, the significance of the contributions, and the originality of the research. Give special consideration to how effectively the paper addresses its limitations and whether it offers adequate context for its assertions. Analyze the degree to which the experimental design—particularly the selection of datasets and tasks—bolsters the findings. Decide if the paper’s strengths significantly surpass its weaknesses, particularly in terms of its potential impact on the field and its appropriateness for acceptance at an academic conference. Deliver a comprehensive evaluation of the paper's contributions and relevance. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", ]


    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "model": "gpt-4.1-nano", "eval_model": "gpt-4.1-nano", "out": "results/eval-240/test_top_prompts.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.7, "optimizer": "nl-gradient", "rounds": 8, "beam_size": 5, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "bf", "scorer": "01", "eval_rounds": 5, "eval_prompts_per_round": 6, "samples_per_eval": 8, "c": 2.0, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)
    # test_exs = task.get_test_examples('data/additional_test_data_800+800.csv')
    test_exs = task.get_test_examples('data/metareviewer_data_test_200.csv')
            

    for _ in range(4):   
        # test_batch = random.sample(test_exs, k=args["n_test_exs"])
        test_batch = test_exs
        f1s = []
        for candidate in candidates:
            sub_ids = []
            sub_preds = []
            sub_labels = []             
            for i in range(args["n_test_exs"] // 50):
                ids, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_batch[i * 50 : (i + 1) * 50], 50)
                sub_ids.extend(ids)
                sub_preds.extend(preds)
                sub_labels.extend(labels)
            f1 = f1_score(sub_labels, sub_preds, average='micro')
            print(f"\ncnt_preds: {len(sub_preds)}\n - f1: {f1}\n")
            f1s.append(f1)
        with open(args["out"], 'a') as outf:  
            outf.write(f'{f1s}\n')

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
