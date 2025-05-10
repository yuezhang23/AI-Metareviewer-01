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

    candidates = ['# Task\n"Given the reviews for the following paper, evaluate the overall sentiment and classify it as either positive (Yes) or negative (No). Focus on the reviewers\' overall impressions rather than getting bogged down in specific criticisms. If the reviews contain substantial praise for the paper\'s contributions, clarity, or innovative aspects, classify it as positive, even if there are some noted weaknesses. However, if the reviews predominantly express concerns that significantly overshadow the strengths, classify it as negative. Your classification should reflect the general tone and sentiment conveyed across all reviews." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',]

    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "model": "gpt-4o-mini", "eval_model": "gpt-4o-mini", "out": "results/eval-240/test_top_prompts.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.7, "optimizer": "nl-gradient", "rounds": 8, "beam_size": 5, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 6, "errors_per_gradient": 4, "gradients_per_error": 3, "steps_per_gradient": 2, "mc_samples_per_step": 0, "max_expansion_factor": 6, "engine": "chatgpt", "evaluator": "bf", "scorer": "01", "eval_rounds": 5, "eval_prompts_per_round": 6, "samples_per_eval": 8, "c": 2.0, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)
    # test_exs = task.get_test_examples('data/additional_test_data_800+800.csv')
    test_exs = task.get_test_examples('data/metareviewer_data_test_200.csv')
            

    for _ in range(5):   
        test_batch = random.sample(test_exs, k=args["n_test_exs"])
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
