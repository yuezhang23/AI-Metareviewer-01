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



args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "out": "results/meta_review_-eval72-c1.5.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.6, "optimizer": "nl-gradient", "rounds": 4, "beam_size": 4, "n_test_exs": 200, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "ucb", "scorer": "01", "eval_rounds": 3, "eval_prompts_per_round": 4, "samples_per_eval": 6, "c": 1.5, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 72}

with open(args["out"], 'a') as outf:
    outf.write(json.dumps(args) + '\n')
    
candidates =["# Task\nAnalyze the sentiment expressed in the given reviews for the research paper, concentrating on the general evaluation regarding its chances of being accepted, rejected, or requiring minor revisions. Take into account the reviewers' identification of strengths and weaknesses, as well as their ratings and levels of confidence. When evaluating sentiment, be mindful of the specific wording in the reviews, including indications of enthusiasm, apprehension, or doubt, and how these elements shape the overall perception of the paper's quality and likelihood of acceptance. Deliver a definitive conclusion about the sentiment classification based on these considerations. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
'# Task\nBased on the reviews provided, assess whether the paper in question would be approved for presentation at an academic conference. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\n"Analyze the reviews given for the paper, considering the strengths and weaknesses pointed out by the reviewers. Decide if the overall evaluation indicates that the paper should be approved or denied for presentation at an academic conference, factoring in the importance of the contributions, robustness of the methodology, clarity of presentation, and any identified shortcomings." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\nBased on the provided reviews of the academic paper, analyze its potential for acceptance at a conference by focusing on several essential criteria. Present a clear and succinct evaluation that covers each aspect:\n\n1. **Soundness**: Examine the technical rigor of the research, taking into account the methodology and experimental framework.\n\n2. **Presentation**: Assess the clarity, organization, and overall readability of the paper, including the effectiveness of any visual aids.\n\n3. **Contribution**: Evaluate the originality and importance of the findings relative to the current body of literature.\n\n4. **Strengths and Weaknesses**: Highlight the key strengths that favor acceptance, along with specific weaknesses or limitations that may pose challenges to acceptance.\n\n5. **Overall Rating**: Provide a concluding judgment regarding the likelihood of the paper being accepted, rejected, or deemed borderline. Justify your rating based on the discussed criteria.\n\nPlease ensure your evaluation is direct and encapsulates the core elements of each review without unnecessary elaboration. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']

task = tasks.MetareviewerBinaryTask('data/', 12)
gpt4 = predictors.BinaryPredictor(args)
test_exs = task.get_test_examples(args["data_dir"] + '/metareviewer_data_test_200.csv')
# test_exs = task.get_test_examples(args["data_dir"] + '/00metareviewer_data_balanced.csv')

for candidate in candidates:
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