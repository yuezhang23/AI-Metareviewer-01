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
    # candidates = ['# Task: Given the reviews (Text), conduct a rigorous evaluation of the proposed research, focusing on theoretical contributions, empirical validation, and practical implications. Assess the innovation and significance of the methodology, the clarity of the presentation, and the relevance of experimental results. Provide a definitive recommendation for acceptance (Yes) or rejection (No), while identifying key strengths, weaknesses, and actionable suggestions for enhancing the impact and applicability of the research. Ensure that your assessment is comprehensive, insightful, and well-supported by evidence from the reviews, addressing any significant concerns raised by the reviewers." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']
    candidates = [
        "# Task: Examine the provided reviews meticulously, focusing on key aspects such as the soundness of the presented methods, presentation quality, and overall contributions to the field of federated learning. Identify notable strengths despite any outlined weaknesses and consider whether the overall evaluation leans towards acceptance of the paper. Based on this balanced assessment, determine if the combined positive sentiments justify a conclusion of Yes for acceptance or No for rejection. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task: Evaluate the peer assessments of the research paper by synthesizing the strengths related to originality, clarity, and empirical results while addressing the cited weaknesses. Determine if the research's merits convincingly advocate for approval despite any identified shortcomings, which should not detract significantly from the paper’s overall value. Conclude with 'Yes' if the strengths substantially support acceptance; otherwise, respond with 'No' for rejection. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:"]
        # human
        # "# Task: Given the following reviews (text), determine accepted (Yes) or not (No). \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-t0.83
        # "# Task: Assess the reviews of the academic paper with a focus on originality, methodological rigor, and practical significance of the proposed contributions. Evaluate whether the strengths claimed by reviewers substantially outnumber the weaknesses, particularly in terms of clarity, empirical validation, and tangible impact on the field. Render a final decision regarding the paper's acceptance status—indicating either Yes for acceptance or No for rejection—based on the overall positive outcomes measured against the critiques, enhancing the decision with an optimistic yet objective tone on its future implications. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-t0.84
        # "# Task: Assess the reviews of the academic paper with a focus on originality, methodological rigor, and practical significance of the proposed contributions. Evaluate whether the strengths claimed by reviewers substantially outnumber the weaknesses, particularly in terms of clarity, empirical validation, and tangible impact on the field. Render a final decision regarding the paper's acceptance status—indicating either Yes for acceptance or No for rejection—based on the overall positive outcomes measured against the critiques, enhancing the decision with an optimistic yet objective tone on its future implications. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # OPRO-t0.85
        # "# Task: Evaluate the reviews of the academic paper by analyzing the originality, empirical contributions, methodological approaches, and clarity of communication. Determine whether the highlighted strengths surpass the identified weaknesses to warrant a positive recommendation. Conclude with a decision on acceptance (Yes) or rejection (No), substantiating your conclusion with clear references to notable strengths and weaknesses identified in the reviews. Emphasize a positive tone regarding contributions to the field and encourage further exploration or development of the proposed ideas. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # OPRO-0.79
        # "# Task: Conduct a thorough evaluation of the criticisms and praises presented in the reviews of the academic paper, zeroing in on the novelty, experimental framework, and illustrative clarity. Assess whether the positive critiques significantly outweigh the negative remarks, especially in the context of broader applicability and methodological contribution. Derive a decision indicating acceptance (Yes) or rejection (No) based on the overall analysis of the paper’s merits in advancing its field. Use moderately optimistic language regarding its implications and potential impact while maintaining objectivity. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # APO- 0.75
        # '# Task\n"Analyze the following reviews (text) and determine if the paper would be accepted (Yes) or rejected (No) by an academic conference. Consider the overall sentiment and narrative formed by the critiques provided by the reviewers. Focus on their assessments of soundness, presentation, and contribution, as well as the significance of the strengths and weaknesses mentioned. Pay close attention to the ratings given, especially those that are \'marginally below the acceptance threshold,\' as these indicate that the paper has merit and should be evaluated with nuance. Look for patterns in the reviewers\' consensus or differing opinions, and consider how the collective feedback indicates a leaning towards acceptance or rejection. Justify your decision based on these insights from the reviews, ensuring that you account for the context of the ratings provided." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',]
        # '# Task\n"Evaluate the following reviews (text) to determine whether the paper is likely to be accepted (Yes) or rejected (No) by an academic conference. Focus primarily on the overall sentiment conveyed in the reviews, considering the strengths and weaknesses highlighted by the reviewers. Take into account the reviewers\' confidence levels and how they weigh the positive aspects against the negative critiques. Pay special attention to instances where significant positive contributions or strong experimental results may outweigh the weaknesses mentioned. Provide a clear justification for your decision that reflects this overall sentiment and balance in evaluations." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',]

    
    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "model": "gpt-4o-mini", "eval_model": "gpt-4o-mini", "out": "results/eval-240/test_top_prompts.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.7, "optimizer": "nl-gradient", "rounds": 8, "beam_size": 5, "n_test_exs": 300, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "bf", "scorer": "01", "eval_rounds": 5, "eval_prompts_per_round": 6, "samples_per_eval": 8, "c": 2.0, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}

    # List of test files to evaluate
    test_files = [
        # 'data/additional_data_800+800_2023_NeurIPS.csv',
        # 'data/additional_data_800+800_2024_NeurIPS.csv',
        # 'data/additional_data_800+800_2024_ICLR.csv',   
        # 'data/additional_data_800+800_2025_ICLR.csv',
        # "data/metareviewer_data_train_800.csv",
        # "data/metareviewer_data_test_200.csv",
        'data/balanced_150+150_neurips_2023.csv'
    ]

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)

    for test_file in test_files:
        print(f"\nEvaluating on file: {test_file}")
        with open(args["out"], 'a') as outf:
            outf.write(f'\nEvaluating on file: {test_file}\n')
        
        test_exs = task.get_test_examples(test_file)

        for _ in range(1):   
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
            print(f1s)
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
