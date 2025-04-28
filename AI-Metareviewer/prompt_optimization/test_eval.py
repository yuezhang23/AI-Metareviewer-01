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

args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "out": "results/mr-eval240-c1.5-01.out", "max_threads": 10, "temperature": 0.0, "expansion_temperature": 0.6, "optimizer": "nl-gradient", "rounds": 6, "beam_size": 4, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "ucb", "scorer": "01", "eval_rounds": 3, "eval_prompts_per_round": 8, "samples_per_eval": 10, "c": 1.5, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}

with open(args["out"], 'a') as outf:
    outf.write(json.dumps(args) + '\n')
    
candidates = ["# Task\nEvaluate the provided reviews to ascertain if the paper is appropriate for presentation at an academic conference. Take into account the following factors: \n1. **Validity**: Are the methodologies and conclusions logically valid and thoroughly backed by evidence?\n2. **Clarity**: Is the paper structured well, coherent, and easy to comprehend?\n3. **Impact**: Does the paper offer a meaningful contribution to its discipline or tackle significant research issues?\n4. **Pros and Cons**: Do the strengths sufficiently outweigh any noted weaknesses?\n5. **Final Recommendation**: What is the overall opinion of the reviewers regarding the paper's acceptance? \n\nUsing these factors, classify the paper as 'Yes' if it is suitable or 'No' if it is not suitable for presentation. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", 
              '# Task\n"Assess the provided reviews of the paper and determine whether it meets the criteria for acceptance at an academic conference. Consider the following aspects: soundness of the methodology, clarity of presentation, originality of contribution, strengths and weaknesses identified in the reviews, and overall rating. Use these factors to decide if the paper is suitable for presentation at the conference." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
              "# Task\nReview the evaluations of the manuscript and decide whether it is appropriate for presentation at an academic conference. In your assessment, take into account the following factors:\n\n1. **Validity**: Are the research methods and findings scientifically credible and well-supported?\n2. **Clarity**: Is the manuscript articulated clearly and structured logically, facilitating reader comprehension?\n3. **Originality**: Does the work present valuable new insights, methodologies, or discoveries that contribute to the field?\n4. **Pros and Cons**: What major strengths and weaknesses have been highlighted in the evaluations?\n5. **Constraints**: Are the study's limitations thoroughly addressed, and do they influence the overall conclusions?\n\nBased on these considerations, provide your final recommendation: 'Accept', 'Reject', or 'Revise and Resubmit'. Please include a concise rationale for your choice. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", 
              "# Task\nExamine the given reviews to assess if the paper qualifies for acceptance at an academic conference. Evaluate the reviewers' feedback regarding technical soundness, clarity of presentation, and the paper's contributions to the field. Focus on the subtleties in the critiques, including both the strengths and weaknesses pointed out by the reviewers, and evaluate how these elements impact the overall assessment. Be aware of any differing viewpoints and the rationale for the ratings, particularly when the paper's evaluation is just above or below the acceptance cutoff. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:"]


task = tasks.MetareviewerBinaryTask('data/', 8)
gpt4 = predictors.BinaryPredictor(args)
test_exs = task.get_test_examples(args["data_dir"] + '/metareviewer_data_test_200.csv')
# test_exs = task.get_test_examples(args["data_dir"] + '/00metareviewer_data_balanced.csv')

ids_lables_totals = {}
for ex in test_exs:
    ids_lables_totals[ex['id']] = ex['label']
        
metrics = []
for candidate in candidates:
    # f1s = 0.0

    trials_ids_preds = {}
    for _ in range(5):   
        sub_ids = []
        sub_preds = []
        for i in range(args["n_test_exs"] // 50):
            ids, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs[i * 50 : (i + 1) * 50])
            sub_ids.extend(ids)
            sub_preds.extend(preds)

        # for each id, add the pred to the list of preds
        for id, pred in zip(sub_ids, sub_preds):
            if id not in trials_ids_preds:
                trials_ids_preds[id] = [pred]
            trials_ids_preds[id].append(pred)

    for id, preds in trials_ids_preds.items():
        if len(preds) == 5:
            ids_lables_totals[id].append(max(set(preds), key=preds.count))
        else:
            del trials_ids_preds[id]
            del ids_lables_totals[id]

    pairs = ids_lables_totals.values() 
    true_labels = [pair[0] for pair in pairs]
    pred_labels = [pair[1] for pair in pairs]
    
    print(f"len(actual_labels): {len(true_labels)} VS len(pred_labels): {len(pred_labels)}")
    f1 = float(f1_score(true_labels, pred_labels, average='micro'))
    metrics.append(f1)
    with open(args["out"], 'a') as outf:  
        outf.write(f'{metrics}\n')
