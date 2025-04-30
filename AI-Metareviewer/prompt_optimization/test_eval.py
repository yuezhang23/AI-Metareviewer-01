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
    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "out": "results/eval_test.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.6, "optimizer": "nl-gradient", "rounds": 6, "beam_size": 4, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "ucb", "scorer": "01", "eval_rounds": 3, "eval_prompts_per_round": 8, "samples_per_eval": 10, "c": 1.5, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}

    with open(args["out"], 'a') as outf:
        outf.write(json.dumps(args) + '\n')

    # Round - from prior results
    candidates = ['# Task\nYour task is to evaluate whether the final decision (accept or reject) in a peer review is justified by analyzing the review’s content, reasoning, evidence, and overall assessment. Determine if the review’s reasoning accurately reflects the paper’s core contributions, if the evidence and analysis support the conclusion, and whether the review fairly and correctly interprets the work. Refrain from judging based solely on tone, length, or superficial factors. Confirm whether the review’s rationale logically underpins the decision.\n\nRespond with **"Correct"** if the decision is well-supported and justified by the reasoning and evidence provided. Respond with **"Incorrect"** if the decision is unfounded, flawed, or if the review misinterprets or overlooks important aspects of the paper or its reasoning.\n\nUse the provided review examples to inform your assessment of proper and improper evaluations.\n\nOutput: \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\nYour task is to evaluate each peer review and determine whether the final decision (accept or reject) is justified based solely on the review’s content, reasoning, and evidence provided. Focus on whether the review’s analysis logically supports its final decision, considering the coherence, consistency, and adequacy of its arguments and evidence.\n\nRespond with only **"Correct"** if the final decision aligns appropriately with the review’s reasoning and evidence, or **"Incorrect"** if it does not. Do not include any explanations or additional comments.\n\nWhen assessing, carefully analyze whether the review’s reasoning directly supports its final judgment, taking into account any nuances, caveats, or implicit assumptions. Do not rely on superficial cues like tone or length but base your judgment on the logical relationship between the review’s analysis and the final decision.\n\nUse the following examples to calibrate your judgment:\n\n**Example 1:**  \nReview content indicates that the review raises significant concerns about methodology, results, and claims, and the final decision is to reject. The review’s reasoning explicitly points out critical flaws and unsupported claims, so the final decision is justified.  \n**Answer:** Correct\n\n**Example 2:**  \nReview states that the work is promising but has some issues that need clarification; the final decision is to accept conditionally. The review’s reasoning supports the accept decision, noting that the issues are addressable and do not undermine the core contribution.  \n**Answer:** Correct\n\n**Example 3:**  \nReview contains some critiques but ultimately concludes that the work is not yet ready for acceptance, recommending rejection. The reasoning provided does not sufficiently justify rejection, as the issues are minor or addressable, yet the final decision is reject.  \n**Answer:** Incorrect\n\n**Example 4:**  \nReview praises the novelty and thoroughness but notes some limitations; the final decision is accept. The reasoning supports that the work’s strengths outweigh the limitations, so the decision is justified.  \n**Answer:** Correct\n\nUse only **"Correct"** or **"Incorrect"** in your response. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\nYour task is to evaluate whether the final decision (accept or reject) in a peer review is justified by analyzing the review’s content, reasoning, evidence, and overall assessment. Determine if the review’s reasoning accurately reflects the paper’s core aspects, whether the evidence and analysis support the conclusion, and if the review fairly and correctly interprets the work. Refrain from judging based solely on tone, length, or superficial characteristics. Confirm whether the review’s rationale logically underpins the final decision.\n\nReply with **"Correct"** if the decision is well-supported and justified by the review’s reasoning and evidence. Reply with **"Incorrect"** if the decision is unsubstantiated, flawed, or if the review misinterprets or overlooks critical elements of the paper or its reasoning.\n\nUse the provided review examples as a reference for appropriate and inappropriate evaluations.\n\nOutput: \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']


    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)
    test_exs = task.get_test_examples(args["data_dir"] + '/metareviewer_data_test_200.csv')
    dupl_test_exs = test_exs + test_exs
            
    metrics = []
    for candidate in candidates:
        ids_lables_totals = {}
        for ex in test_exs:
            ids_lables_totals[ex['id']] = [ex['label']]  

        trials_ids_preds = {}
        for _ in range(1):   
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
            outf.write(f'Step {i}: {f1}\n') 

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
