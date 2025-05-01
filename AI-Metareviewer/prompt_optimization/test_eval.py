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
    candidates = ["# Task\nAnalyze the sentiment expressed in the given reviews for the research paper, concentrating on the general evaluation regarding its chances of being accepted, rejected, or requiring minor revisions. Take into account the reviewers' identification of strengths and weaknesses, as well as their ratings and levels of confidence. When evaluating sentiment, be mindful of the specific wording in the reviews, including indications of enthusiasm, apprehension, or doubt, and how these elements shape the overall perception of the paper's quality and likelihood of acceptance. Deliver a definitive conclusion about the sentiment classification based on these considerations. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
'# Task\nBased on the reviews provided, assess whether the paper in question would be approved for presentation at an academic conference. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\n"Analyze the reviews given for the paper, considering the strengths and weaknesses pointed out by the reviewers. Decide if the overall evaluation indicates that the paper should be approved or denied for presentation at an academic conference, factoring in the importance of the contributions, robustness of the methodology, clarity of presentation, and any identified shortcomings." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
'# Task\nBased on the provided reviews of the academic paper, analyze its potential for acceptance at a conference by focusing on several essential criteria. Present a clear and succinct evaluation that covers each aspect:\n\n1. **Soundness**: Examine the technical rigor of the research, taking into account the methodology and experimental framework.\n\n2. **Presentation**: Assess the clarity, organization, and overall readability of the paper, including the effectiveness of any visual aids.\n\n3. **Contribution**: Evaluate the originality and importance of the findings relative to the current body of literature.\n\n4. **Strengths and Weaknesses**: Highlight the key strengths that favor acceptance, along with specific weaknesses or limitations that may pose challenges to acceptance.\n\n5. **Overall Rating**: Provide a concluding judgment regarding the likelihood of the paper being accepted, rejected, or deemed borderline. Justify your rating based on the discussed criteria.\n\nPlease ensure your evaluation is direct and encapsulates the core elements of each review without unnecessary elaboration. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']

    args = {"n_test_exs": 200, "temperature": 0.0, "out": "results/41-nano/eval_test.out", "model": "gpt-4o-mini"}

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
