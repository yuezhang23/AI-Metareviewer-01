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



args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "out": "results/meta_review_00.out", "max_threads": 6, "temperature": 0.0, "expansion_temperature": 0.6, "optimizer": "nl-gradient", "rounds": 3, "beam_size": 4, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "ucb", "scorer": "01", "eval_rounds": 3, "eval_prompts_per_round": 4, "samples_per_eval": 3, "c": 1.5, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 36}
candidate = '''# Task\nEvaluate the provided reviews of an academic paper to decide if the paper should be accepted, marginally accepted, marginally rejected, or rejected at an academic conference. Your judgment should stem from a thorough assessment of the following specific criteria: soundness, clarity of presentation, contribution to the field, and the strengths and weaknesses noted in each review.\n\nTake into account the overall sentiment expressed in each review and how the identified strengths and weaknesses interact with one another. Be mindful of the ratings and levels of confidence given by the reviewers, as these indicate their degree of certainty in their evaluations.\n\nSummarize the essential points from the reviews and articulate a clear justification for your decision, showing how each review's insights correlate with the overall evaluation criteria while acknowledging any differing perspectives.\n\nConclude with a classification of 'Accept', 'Marginally Accept', 'Marginally Reject', or 'Reject'. Make sure your classification reflects the general consensus among the reviews while addressing any notable differences in their evaluations. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:'''


task = tasks.MetareviewerBinaryTask('data/', 6)
gpt4 = predictors.BinaryPredictor(args)
test_exs = task.get_test_examples()

metrics = []
if (args["n_test_exs"] > 100):
    labels_total = []
    preds_total = []
    for i in range(args["n_test_exs"] // 50):
        _, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs[i * 50 : (i + 1) * 50])
        labels_total.extend(labels)
        preds_total.extend(preds)
    f1 = float(f1_score(labels_total, preds_total, average='micro'))
    metrics.append(f1)
else:
    f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs, n=args["n_test_exs"])
    metrics.append(float(f1))
with open(args["out"], 'a') as outf:  
    outf.write(f'{metrics}\n')