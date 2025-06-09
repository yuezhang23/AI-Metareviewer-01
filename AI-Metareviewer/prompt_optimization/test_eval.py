import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
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
from sklearn.metrics import f1_score, confusion_matrix
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

if __name__ == '__main__':

    # Round - from prior results
    # candidates = ['# Task: Given the reviews (Text), conduct a rigorous evaluation of the proposed research, focusing on theoretical contributions, empirical validation, and practical implications. Assess the innovation and significance of the methodology, the clarity of the presentation, and the relevance of experimental results. Provide a definitive recommendation for acceptance (Yes) or rejection (No), while identifying key strengths, weaknesses, and actionable suggestions for enhancing the impact and applicability of the research. Ensure that your assessment is comprehensive, insightful, and well-supported by evidence from the reviews, addressing any significant concerns raised by the reviewers." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']
    candidates = [
        # OPRO -train0.74/0.72/0.70/0.70/0.675/0.654/0.61
        # "# Task\nCritically assess the reviews of the academic paper, focusing on the significance of the advancements in theory and empirical methods as evaluated by the reviewers. Monitor the assessment of strengths versus weaknesses to ascertain whether the merits clearly justify a decision for acceptance (Yes) or indicate grave concerns for rejection (No). Your conclusion must reference precise examples and insights from the reviews for support, discerning whether they collectively point towards a promising contribution to the field. Maintain an encouraging tone, illuminating the potential trajectory of the research and possible future explorations.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nAssess the provided academic paper reviews, focusing on the contributions made through the Heterophilous Message-Passing mechanism and the proposed Compatibility Matrix-aware GNN framework. Analyze if the strengths outlined by reviewers significantly offset the acknowledged weaknesses, seeking to arrive at a final recommendation of acceptance (Yes) or rejection (No). Your decision should be firmly grounded in an analytical synthesis of feedback that highlights positively framed comments while addressing potential areas of concern in a constructive manner. Ensure your assessment maintains an engaging and motivated outlook regarding the paper's relevance and attractiveness within its academic domain.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nConduct a thorough analytic review of the academic paper evaluations, examining key strengths, methodological clarity, and the overall impact on the field of study. Evaluate the likelihood that the strengths highlight a significantly impactful contribution that outweighs any documented weaknesses. Make a definitive acceptance determination by choosing either Yes for acceptance or No for rejection. Support this decision with detailed, evidential justifications that underline favorable critiques and thoughtfully discuss any concerns, all while conveying an optimistic outlook on the potential of the research to advance understanding within its domain.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nEvaluate the provided reviews of the research paper by considering factors such as the novelty of the proposed task, clarity of the articulation of results, solidity of methodology, and impact in the multi-domain anomaly detection area. Analyze the strengths articulated alongside the limitations present, determining if the strengths signify promise and innovative contribution that can outweigh the weaknesses, thus leading to a recommendation for final acceptance (Yes) or rejection (No) based on the majority viewpoint communicated by the reviewers.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nEvaluate the reviews of the research paper by examining the novel contributions to vision-language models, assessing the completeness of theoretical justification, clarity of presentation, and how well experimental results substantiate the claims made. Deliberate carefully on strengths and weaknesses identified by the reviewers, determining if the overall sentiment tips in favor of acceptance despite areas for improvement. Provide a final decision as 'Yes' for acceptance or 'No' for rejection based on this comprehensive overview.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nAnalyze the reviews of the research paper by evaluating the originality, practical contributions, and the clarity of the proposed approach. Take note of both strengths and weaknesses discussed by reviewers, placing special emphasis on experimental results and their implications for generalization in the respective field. Ultimately, determine if the benefits of the proposed method clearly justify its limitations, leading to a recommendation of Yes for acceptance or No for rejection based on the prevailing nature of the sentiments in the critiques.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]
        # "# Task\nEvaluate the provided reviews of the research paper while focusing on the originality of the contributions, clarity in results presentation, and soundness of methodologies employed. Analyze the feedback to ascertain whether the recognized strengths substantively exceed any highlight weaknesses derived from the critiques. Based on this comprehensive evaluation, provide a final recommendation labeling the paper with 'Yes' for acceptance or 'No' for rejection, reflecting the reviewers' general sentiment toward the work's significance and impact.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",]  
        
        # APO 0
        '# Task\n"Assess the reviews provided for the paper and determine whether it should be accepted at an academic conference. Take into account the strengths, weaknesses, soundness, presentation, and contribution ratings, while being mindful that a paper may still warrant acceptance despite having some weaknesses. Focus on the overall impact and significance of the contributions, as well as how effectively the paper addresses its challenges. Your assessment should reflect a nuanced understanding of the review process, leading to a thoughtful recommendation based on the balance of merits and shortcomings."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO 1
        '# Task\n"Evaluate the reviews of the submitted paper, considering the reviewers\' assessments of strengths, weaknesses, soundness, presentation, and contribution. Focus on the nature of the weaknesses: if they point to areas for improvement rather than critical flaws, weigh this insight in your classification. Ensure your conclusion encompasses the paper\'s theoretical contributions as well as its practical applications, while also aligning your judgment with the goals of the conference and the overall significance of the work in its domain."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO 2
        '# Task\n"Evaluate the reviews provided for the paper and determine whether it should be accepted at an academic conference. When making your assessment, weigh the strengths against the weaknesses, considering the overall soundness, presentation, and contributions of the paper. Recognize that a paper may still be worthy of acceptance even if it has notable weaknesses, especially if the contributions are significant and the weaknesses are manageable. Your recommendation should reflect a balanced view that integrates the merits and shortcomings, focusing on the paper\'s overall impact and relevance to the field."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO 3
        '# Task\n"Analyze the following reviews (text) and determine if the paper would be accepted (Yes) or rejected (No) by an academic conference. Consider the overall sentiment and narrative formed by the critiques provided by the reviewers. Focus on their assessments of soundness, presentation, and contribution, as well as the significance of the strengths and weaknesses mentioned. Pay close attention to the ratings given, especially those that are \'marginally below the acceptance threshold,\' as these indicate that the paper has merit and should be evaluated with nuance. Look for patterns in the reviewers\' consensus or differing opinions, and consider how the collective feedback indicates a leaning towards acceptance or rejection. Justify your decision based on these insights from the reviews, ensuring that you account for the context of the ratings provided." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO 4
        '# Task\n"Evaluate the following reviews (text) to determine whether the paper is likely to be accepted (Yes) or rejected (No) by an academic conference. Focus primarily on the overall sentiment conveyed in the reviews, considering the strengths and weaknesses highlighted by the reviewers. Take into account the reviewers\' confidence levels and how they weigh the positive aspects against the negative critiques. Pay special attention to instances where significant positive contributions or strong experimental results may outweigh the weaknesses mentioned. Provide a clear justification for your decision that reflects this overall sentiment and balance in evaluations." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO 5
        '# Task\n"Classify the following reviews of a submitted paper as accepted (\'Yes\') or rejected (\'No\'). Carefully analyze the numerical ratings and qualitative feedback from the reviewers. Pay attention to the overall sentiment about the paper\'s contributions, considering both strengths and weaknesses. If the reviews highlight significant strengths and contributions, they should be given considerable weight in favor of acceptance, regardless of criticisms. Ensure that your classification reflects the comprehensive sentiment of the reviews, and provide a summary that details the key strengths and weaknesses, explaining how they influenced your final decision. Aim to provide a balanced assessment that recognizes the merits of the submission while considering the critiques." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO-6
        "# Task: Critically evaluate the provided reviews of the academic paper, focusing on the assessment of the innovative approach, methodological soundness, and clarification of results. Author remarks on the strengths and weaknesses should be synthesized to determine if the overall contributions substantially outweigh the drawbacks, culminating in a clear recommendation for acceptance (Yes) or rejection (No). Emphasize the optimistic aspects of the review while effectively addressing any given critiques to justify your conclusion. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-7
        "# Task: Evaluate the reviews of the academic paper by thoroughly analyzing the significance and novelty of its contributions, methodologies employed, and implications for future research. Focus on identifying whether the positive feedback significantly outweighs the criticisms noted by reviewers. Along with your explanation, provide a definitive acceptance recommendation—Yes for acceptance or No for rejection—while ensuring a tone of optimism and emphasizing the relevance of the research to advancements in the field. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-8
        "# Task: Conduct a detailed examination of the reviews pertaining to the academic paper, emphasizing the paper's originality, methodological excellence, and empirical validations. Evaluate whether the strengths highlighted by the reviewers distinctly surpass the identified weaknesses, particularly in relation to clarity of presentation and its contribution to the research area. Make a final decision regarding acceptance (Yes) or rejection (No), grounded in a balanced yet optimistic interpretation of the paper's potential significance and implications on future research directions. Use affirmative language that reflects positivity and reassurance about the paper's capabilities to advance its field. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-9
        "# Task: Critically assess the reviews of the academic paper, focusing on the significance of the advancements in theory and empirical methods as evaluated by the reviewers. Monitor the assessment of strengths versus weaknesses to ascertain whether the merits clearly justify a decision for acceptance (Yes) or indicate grave concerns for rejection (No). Your conclusion must reference precise examples and insights from the reviews for support, discerning whether they collectively point towards a promising contribution to the field. Maintain an encouraging tone, illuminating the potential trajectory of the research and possible future explorations. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-10
        "# Task: Conduct a thorough analytic review of the academic paper evaluations, examining key strengths, methodological clarity, and the overall impact on the field of study. Evaluate the likelihood that the strengths highlight a significantly impactful contribution that outweighs any documented weaknesses. Make a definitive acceptance determination by choosing either Yes for acceptance or No for rejection. Support this decision with detailed, evidential justifications that underline favorable critiques and thoughtfully discuss any concerns, all while conveying an optimistic outlook on the potential of the research to advance understanding within its domain. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO-11
        "# Task: Evaluate the provided reviews of the academic paper by analyzing its conceptual contributions, robustness of findings, and clarity of exposition. Determine if the positive aspects the reviewers recognize in relation to the proposed methods and theoretical foundations significantly outweigh any limitations or critiques presented. Based on this analysis, issue a final recommendation of acceptance (Yes) or rejection (No), thoroughly justifying your decision with explicit references to strengths, weaknesses, and contextual relevance to advance understanding in the field. Maintain a forward-looking and supportive perspective that highlights areas for future exploration or improvement. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        ]

    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "model": "gpt-4o-mini", "eval_model": "gpt-4o-mini", "out": "results/eval-240/test_top_prompts.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.7, "optimizer": "nl-gradient", "rounds": 8, "beam_size": 5, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "bf", "scorer": "01", "eval_rounds": 5, "eval_prompts_per_round": 6, "samples_per_eval": 8, "c": 2.0, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}
    # Print all candidates

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    # List of test files to evaluate
    test_files = [
        # 'data/additional_data_800+800_2023_NeurIPS.csv',
        # 'data/additional_data_800+800_2024_NeurIPS.csv',
        # 'data/additional_data_800+800_2024_ICLR.csv',   
        # 'data/additional_data_800+800_2025_ICLR.csv',
        # "data/metareviewer_data_train_800.csv",
        # 'data/100+100_neurips_2024_train.csv',
        # "data/metareviewer_data_test_200.csv",
        # 'data/25+100_neurips_2024_test.csv',
        # 'data/100+25_neurips_2024_test.csv',
        # 'data/100+100_neurips_2023_test.csv',
        'data/40+160_neurips_2023_test.csv',
        # 'data/160+40_neurips_2023_test.csv',
        # 'data/178+22_neurips_2023_test.csv',
        # 'data/33+100_neurips_2024_test.csv',
        # "data/140+140_neurips_2024_test.csv",
    ]

    # pick candidate index 6, 7,8 for 140+140_neurips_2024_test.csv
    candidate_indices = [11]

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)

    for test_file in test_files:
        print(f"\nEvaluating on file: {test_file}")
        with open(args["out"], 'a') as outf:
            outf.write(f'\nEvaluating on file: {test_file}\n')
        
        test_exs = task.get_test_examples(test_file)

        for _ in range(1):   
            test_batch = test_exs
            # test_batch = random.sample(test_exs, 900)
            f1s = []
            for j in candidate_indices:
                candidate = candidates[j]
                print(f"\nEvaluating on candidate: {candidate}")
                sub_ids = []
                sub_preds = []
                sub_labels = []             
                for i in range((len(test_exs) + 49) // 50):
                    ids, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_batch[i * 50 : (i + 1) * 50], 50)
                    sub_ids.extend(ids)
                    sub_preds.extend(preds)
                    sub_labels.extend(labels)
                f1 = f1_score(sub_labels, sub_preds, average='micro')
                print(f"\ncnt_preds: {len(sub_preds)}\n - f1: {f1}\n")
                f1s.append(f1)
                
                # Create and plot confusion matrix
                cm = confusion_matrix(sub_labels, sub_preds)
                plt.figure(figsize=(8, 6))
                sns.heatmap(cm, annot=True, fmt='d', cmap='Blues',
                           xticklabels=['No', 'Yes'],
                           yticklabels=['No', 'Yes'])
                plt.title(f'Confusion Matrix - Candidate {j}')
                plt.ylabel('True Label')
                plt.xlabel('Predicted Label')
                
                # Save the confusion matrix plot
                plt.savefig(f'results/cm/confusion_matrix_candidate_train_4v1_candidate_{j}_neurips_2023_test.png')
                plt.close()
                
            with open(args["out"], 'a') as outf:  
                outf.write(f'{f1s}\n')
            print(f1s)
            
