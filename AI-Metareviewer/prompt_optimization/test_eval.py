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
from sklearn.metrics import f1_score, confusion_matrix, accuracy_score
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
        '# Task\n"Evaluate the following reviews to determine the likelihood of acceptance (Yes) or rejection (No) of the paper by an academic conference. Focus on the specific strengths and weaknesses pointed out by the reviewers, particularly emphasizing the originality, theoretical contributions, and empirical validation of the research. Assess how well the positive aspects of the paper counterbalance the criticisms raised. Take into account the reviewers\' confidence levels and ensure that your conclusion is well-supported by the review content, justifying the decision based on a comprehensive analysis of both favorable and unfavorable comments." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
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
        '# Task: Critically review the peer evaluations of the research paper on neural generator models for molecular design, systematically highlighting each reviewer\'s emphasized points regarding the scientific merit, innovative contributions, and applied methodologies. Assess how the paper\'s qualities outweigh identified shortcomings while ensuring the insights presented are grounded in coherent reasoning derived from reviewer critiques. Notably, make an evaluative judgment advocating for acceptance (Yes) or rejection (No) backed by substantial evidence captured in the reviews, recognizing any suggestions for improvement. Your conclusion should succinctly reflect a deepened understanding of the work\'s significance within molecular generation discusssions.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO-12
        '# Task: Assess the reviews of the provided research paper by critically examining the strengths, weaknesses, and overall contributions presented, with a particular focus on evaluating its impact and relevance in the field. Provide a well-supported recommendation for acceptance (Yes) or rejection (No) based on a clear synthesis of reviewer comments, making sure to substantiate the final verdict by highlighting notable strengths that significantly counterbalance any identified flaws. Ensure the analysis reflects both quantitative metrics (such as numerical ratings) and qualitative assessments drawn from the reviews. Summarize in a conclusive statement that affirms the decision and conveys the paper\'s importance within its academic context.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO-13
        '# Task: Evaluate the given peer reviews of a research paper to form an integrated conclusion about its potential for acceptance (Yes) or rejection (No). Analyze the criticism and praise provided by the reviewers, particularly emphasizing the insights regarding innovative contributions, soundness of methodology, clarity of presentation, and the relevance to current challenges in the field. Ensure that the final decision is strongly justified using specific examples from the reviews to highlight the paper\'s overall merit and areas of concern that may influence its acceptance." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO-14
        '# Task: Evaluate the peer reviews of the research paper with attention to the originality, practicality, and scholarly significance of the proposed model editing method. Focus on the reviewers\' assessments of strengths, weaknesses, and suggested improvements, and decide whether the merits of the paper justifiably lead to a recommendation for acceptance (Yes) or a rejection (No). Your analysis should synthesize key arguments from the reviews and make a well-supported case for your final determination, outlining how the paper contributes to the advancement of knowledge in the field while addressing valid concerns raised by reviewers.\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO - 15  
        '# Task\n"Assess the following reviews of a submitted paper to classify it as accepted (\'Yes\') or rejected (\'No\'). Carefully consider the overall sentiment expressed in the reviews, including both strengths and weaknesses. Pay particular attention to the numerical ratings, qualitative feedback, and the confidence levels of the reviewers. While individual criticisms are important, ensure that you weigh the positive aspects and contributions of the paper against them. If the reviews highlight significant strengths that suggest the paper has merit despite criticisms, lean towards acceptance. Summarize your decision with a rationale that emphasizes the overall sentiment, the balance of strengths and weaknesses, and the reviewers\' confidence in their evaluations." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 16
        '# Task: Conduct a thorough analysis of the peer reviews of the research paper focusing on the innovation presented, the empirical robustness of the methodology, and the potential applications of the findings. Evaluate how the strengths mentioned by reviewers are substantial enough to counter the limitations they have also highlighted, considering aspects like coherence, originality, and relevance to the field. Your final decision should reflect a balanced synthesis of these views, ultimately articulating a recommendation of acceptance (Yes) or rejection (No) supported with explicit references to reviewer comments to underline your conclusion." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 17
        '# Task: Analyze the reviews of the academic paper focusing on the methodologies applied, the clarity of the contributions to existing knowledge, and the overall presentation of ideas. Assess if the pros of the research sufficiently mitigate the cons as presented in the critiques, providing a balanced recommendation for either acceptance (Yes) or rejection (No). Support the conclusion with an insightful summary that emphasizes the strengths and addresses the weaknesses candidly, promoting a positive perspective on the paper\'s potential implications in its field." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # from claude recommendations
        # OPRO - 18
        '# Task: Analyze the peer reviews of the research paper to convey a nuanced judgment regarding its acceptance (Yes) or rejection (No). Emphasize unique findings, theoretical advancements, and practical implications when applicable. Weigh the reviewed strengths against limitations raised, prioritizing clarity, methodological soundness, and relevance to the conference, with a rationale substantiated through cited reviewer feedback." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 19
        '# Task: Summarize the reviews provided for the research paper, highlighting its key contributions, soundness, and clarity of presentation. Analyze the strengths that support an acceptance (Yes) against any significant weaknesses that may lead to rejection (No). Provide a direct recommendation for acceptance or rejection of the paper, grounded in the synthesis of reviewer evaluations." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 20
        '# Task: Summarize the reviews of the research paper, highlighting the strengths and weaknesses identified by reviewers. Focus on the overall contribution to the field, theoretical innovation, experimental validation, and methodological consistency. Provide a clear recommendation for acceptance (Yes) or rejection (No) based on whether the outcomes satisfy academic standards for quality, clarity, and overall significance." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 21
        '# Task: Analyze the peer reviews of the research paper and produce a thorough recommendation for acceptance (Yes) or rejection (No). Focus on evaluating the significance of the contributions, innovation, rigor, and practical implications presented in the work. Review both the strengths and criticisms highlighted by the reviewers, ensuring a balanced assessment." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 22
        '# Task: Summarize the reviewer evaluations of the research paper, accentuating key aspects such as innovative methodologies, empirical validation, theoretical implications, and clarity of results. Assess the overall contribution against identified weaknesses. Develop a conclusive recommendation for acceptance (Yes) or rejection (No)." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO - 23 0.71
        '# Task\n"Given the following reviews of an academic paper, assess whether the paper should be accepted (Yes) or rejected (No). Consider the overall tone and context of the reviews, weighing the strengths and weaknesses mentioned. Even if there are some weaknesses highlighted, if the overall consensus is positive and the contributions are significant, the paper may still warrant acceptance."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO - 24 0.71
        '# Task\n"Based on the following reviews of an academic paper, determine the overall likelihood of acceptance. Take into account key strengths, such as innovative contributions and thorough experimental validation, alongside any weaknesses raised by the reviewers. Assess how the positive aspects may balance or overshadow the negative critiques."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO - 25 0.715
        '# Task\n"Review the following text and categorize the paper\'s outcome as \'Accepted\' (Yes), \'Rejected\' (No), or \'Borderline\' (might be accepted with revisions) for an academic conference. Pay particular attention to the overall tone of the review, including any specific phrases or indicators that suggest the reviewer\'s nuanced perspective on the paper\'s merits. Assess the strengths and weaknesses mentioned, focusing on the soundness of the methodology, quality of presentation, and contributions to the field. Consider any contrasting statements where a reviewer acknowledges significant weaknesses but still rates the paper as borderline or above. Look for phrases that indicate the reviewer\'s confidence level and overall sentiment towards the potential of the paper despite its flaws."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO -26 0.72
        '# Task\n"Evaluate the provided reviews of the academic paper and determine whether the paper should be accepted (Yes) or rejected (No). In your analysis, prioritize the overall sentiment and collective ratings from all reviewers. Pay attention to the number of reviewers who recommend acceptance and their confidence levels, as well as the context of any criticisms. A paper may still be viewed favorably for acceptance if it receives high ratings from multiple reviewers, even if one or two reviews express concerns. Consider the significance of the paper\'s contributions and whether the strengths identified outweigh the weaknesses. Your final decision should reflect the prevailing sentiment across all reviews, taking into account both praise and criticism."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO - 27 0.73
        '# Task\n"Evaluate the provided reviews of the academic paper and decide if the paper should be accepted (Yes) or rejected (No). While assessing, prioritize the overall sentiment and context of the reviews, particularly focusing on the strengths and weaknesses presented. Consider the significance of the contributions in relation to the criticisms raised, and balance them against the reviewers\' confidence levels. Pay attention to any marginal ratings that may indicate a leaning toward acceptance despite some issues. Your final decision should reflect the overall sentiment, indicating whether the paper is more likely to be accepted or rejected based on its merits."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO - 28 0.75
        '# Task\n"Evaluate the following academic paper reviews and determine whether the paper should be accepted (Yes) or rejected (No). Focus on the strengths and weaknesses identified by the reviewers, particularly those that suggest a significant contribution or potential impact of the paper despite existing concerns. Pay attention to nuanced evaluations, especially those indicating borderline acceptance or rejection, and consider how positive aspects may outweigh negative ones. Your final decision should reflect a comprehensive understanding of the reviews and the overall contribution of the paper within its research domain."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # APO - 29 0.665
        '# Task\n"Assess the likelihood of acceptance (Yes) or rejection (No) for the provided academic paper reviews, emphasizing both quantitative ratings and qualitative feedback. Analyze the strengths, weaknesses, and key insights from each review, considering how they contribute to an overall understanding of the paper\'s significance. Be mindful of cases where individual reviews may present compelling reasons for acceptance that are not fully reflected in their numerical scores. Your classification should balance the consensus among reviewers with the specific merits discussed, ensuring that critical perspectives are integrated into the final assessment. Consider the overall sentiment expressed in the reviews while looking for any contradictions that might indicate a more nuanced outcome." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO - 30 0.74
       '# Task\n"Given the following reviews (text), determine whether a paper would be accepted (Yes) or not (No) by an academic conference. Take into account the overall quality of the reviews, including soundness, presentation, contribution ratings, and confidence levels of the reviewers. Pay close attention to the reviewers\' expressed sentiments, strengths, and weaknesses. Even if some weaknesses are mentioned, assess the overall tone and confidence of the reviews to identify any strong leanings toward acceptance or rejection. Provide a brief justification for your decision based on the information presented in the reviews, highlighting key factors that influenced your classification." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', 
        # APO - 31 0.68
       '# Task\n"Given the following reviews (text), evaluate whether a paper would likely be accepted (Yes) or rejected (No) by an academic conference. Focus on the qualitative aspects of the reviews, particularly the strengths and weaknesses identified by the reviewers, as well as any nuanced comments that suggest potential for improvement or significant contributions to the field. While numerical ratings provide context, prioritize the content of the reviews to assess the overall potential for acceptance. Provide a reason for your decision based on the insights derived from the reviews." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # from claude recommendations
        # OPRO - 32 
        "# Task\n'Analyze the reviews of the academic paper by assessing the clarity of contributions, robustness of methodology, and overall impact on the field. Determine if the identified strengths and positive remarks from reviewers substantially overshadow the critiques. Based on this analysis, provide a final decision on paper acceptance with either Yes for acceptance or No for rejection, ensuring an objective evaluation that captures both optimism about its potential impact and critical engagement with its limitations.' \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:",
        # OPRO - 33
        '# Task\n"Analyze the reviews of the academic paper focusing on the methodologies applied, the clarity of the contributions to existing knowledge, and the overall presentation of ideas. Assess if the pros of the research sufficiently mitigate the cons as presented in the critiques, providing a balanced recommendation for either acceptance (Yes) or rejection (No)." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 34
        '# Task\n"Examine the critiques and strengths articulated in the reviews of the research paper, assessing elements such as theoretical contributions, practical implications, and methodological soundness. Make a determination for acceptance (Yes) or rejection (No) based on an integrated analysis of reviewer feedback, ensuring to explicitly state how the advantages surpass the concerns and providing balanced reasoning that reflects both qualitative insights and any quantitative data. Conclusions should actionable and clearly articulated to highlight both potential impact and any significant limitations identified." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 35
        '# Task\n"Analyze the peer reviews of the research paper to formulate a contextual recommendation for its acceptance (Yes) or rejection (No). Assess the originality of the contributions, empirical validity, and clarity of the results while systematically reviewing specific comments from peer evaluations. Make recommendations based on a net evaluation where strengths are clearly articulated, effectively outweighing acknowledged limitations. Ensure that your final decision carefully integrates the diversity of insights from the reviews, accentuating its scientific significance and endorsing actionable feedback. Aim to consolidate the overall impression that highlights both the impact of the research and realistic evaluations from the reviewers." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 36
        '# Task\n"Evaluate the peer reviews of the research paper with an emphasis on the overall contributions, innovations, and practical implementations within the context of the methodology and subject matter. Identify both compelling strengths that substantiate a case for acceptance (Yes) and critical flaws that undermine the argument for rejection (No). Provide a clear, structured recommendation concluding with a judicious balance of insight drawn from review feedback aligned with established criteria in the field. Your decision should be well-supported with specific examples from the reviewers\' comments to solidify the evaluative outcome and represent a nuanced understanding of the paper\'s relevance and impact." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',  
        # OPRO - 37 0.72
        '# Task\n"Evaluate the provided reviews of the academic paper by examining the perceived strengths and weaknesses highlighted by reviewers. Focus on the contributions to the field, applicability of the methodology, and clarity of presentation. Decide if the total positive feedback suggests that the merits of the paper significantly outweigh its challenges. Based on this analysis, designate the paper as accepted (Yes) or rejected (No)." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 38 0.71
        '# Task\n"Examine the various reviews for the academic paper while focusing on the evaluations of methodological soundness, contribution to the field, and quality of presentation. Analyze if the strengths collectively present a compelling case for acceptance vastly outweighing the criticisms noted by the reviewers. Integrate this analysis with an understanding of the paper\'s potential impact on the research community to arrive at a final decision regarding the paper\'s acceptance—assigning either Yes for acceptance or No for rejection." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 39 0.73
        '# Task\n"Evaluate the reviews of the academic paper by synthesizing the strengths and weaknesses put forth by the reviewers, emphasizing the relevance and innovation of the proposed methods in the context of existing literature. Carefully assess whether the positive aspects, particularly in terms of empirical validation and potential impact, substantially exceed the criticisms taken into account. Finally, issue a definitive recommendation of acceptance (Yes) or rejection (No) based on this comprehensive evaluation along with clear justification for the decision based on the overall contributions and significance to the field." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 40 0.73
        '# Task\n"Conduct a comprehensive assessment of the reviews regarding the academic paper, focusing on the novel contributions, theoretical rigor, and empirical evidence provided. Evaluate whether the strengths identified notably surpass the weaknesses mentioned by the reviewers, particularly emphasizing contributions to the methodology and applicability in the field. Finally, arrive at a conclusive acceptance recommendation of either Yes for acceptance or No for rejection, grounded in a synthesis of insights while highlighting the overall relevance and potential impact on the field." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 41 0.76
        '# Task\n"Evaluate the detailed reviews of the academic paper by scrutinizing the originality, methodology, and influence of the proposed contributions within the context of existing literature. Assess whether the strengths presented in the reviewers\' comments substantially outweigh the criticisms raised, and determine the overall significance of the findings. Formulate a recommendation regarding acceptance (Yes or No), justifying your decision with a clear and constructive analysis that emphasizes both positive aspects and any remedial comments to address weaknesses—while maintaining a supportive and optimistic tone that underscores the potential impact of the research." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 42 0.73
        '# Task\n"Assess the critiques and prominent strengths of the academic paper, focusing on novelty, methodological robustness, and presentation clarity. Determine if the paper\'s contributions notably surpass any identified limitations, fostering growth in the research area. Conclude with a recommendation of acceptance (Yes) or rejection (No), substantiated by a thorough evaluation that emphasizes favorable aspects while constructively addressing critiques in a supportive manner." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 43 0.74
        '# Task\n"Evaluate the critiques and praises stated in the academic paper reviews with particular emphasis on the clarity of methodology, the significance of contributions to the field, and the adequacy of experimental validation. Determine whether the positive aspects substantially overshadow the critiques. Provide a nuanced acceptance judgment (Yes or No), accompanied by explicit and comprehensive justifications that skillfully balance addressing the weaknesses while highlighting the optimistic elements that promise advancements in research." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 44 0.72
        '# Task\n"Conduct a comprehensive assessment of the academic paper reviews, focusing on the clarity and originality of the proposed method, the soundness of the theoretical framework, and the effectiveness demonstrated through empirical results. Determine whether the positive attributes consistently identified by reviewers outweigh the noted areas for improvement. Conclude with a recommendation for acceptance (Yes) or rejection (No), substantiating your decision with critical reflections that highlight encouraging feedback while thoughtfully addressing critiques." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 45 0.74
        '# Task\n"Conduct a focused assessment of the provided academic paper reviews, concentrating on the significance of contributions, the surpassing of offered strengths over stated weaknesses, and overall clarity of presentation. Analyze whether the findings presented in each review sufficiently warrant an affirmative or negative acceptance decision. Provide a justified conclusion that substantiates a decision of acceptance (Yes) or rejection (No), ensuring that the justification amplifies the anticipated contributions and potential future impacts on the field while handily addressing criticisms mentioned within the reviews." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 46 0.75
        '# Task\n"Perform an in-depth analysis of the academic paper based on the provided reviews, focusing on the novelty of the proposed methodology, the thoroughness of the experimental evaluations, and the clarity and organization of the presentation. Consider whether the identified strengths of the paper significantly outweigh the weaknesses noted by reviewers. Ultimately, issue a clear acceptance decision (Yes or No), substantiated by well-reasoned justifications that highlight the merits of the work while addressing any limitations. Ensure the assessment maintains an optimistic perspective regarding the research\'s implications and contributions to the relevant field." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 47 0.81
        '# Task\n"Assess the provided reviews of the academic paper with a precise focus on the theoretical and empirical contributions through a critical lens. Determine whether the strengths asserted by the reviewers substantially overshadow any weaknesses. Make a decisive recommendation for acceptance (Yes) or rejection (No), bolstered by explicit, confident justifications that incorporate positive insights, clarify critical concerns, and add depth to the discussion on the paper\'s potential advances and contributions to its field. Keep a tone that is both optimistic and constructive, aiming to inspire further inquiry into the subject matter." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 48 0.715
        '# Task\n"Assess the provided reviews of the research paper, focusing on the contributions of the work, its innovative aspects, methodological validity, and clarity of presentation. Analyze the reviewers\' comments to determine if the strengths considerably outweigh the identified weaknesses, contradicting the notion of rejection. Based on this evaluation, make a definitive recommendation for acceptance (Yes) or rejection (No), ensuring a clear rationale that reflects the feedback and assesses its overall significance to the field." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 49 0.71
        '# Task\n"Perform a comprehensive analysis of the peer reviews for the research paper on Dinomaly, focusing on elements such as innovation, performance comparisons with existing methods, extended discussions on theoretical contributions, and practicality of the findings. Identify and evaluate how the strengths noted by the reviewers predominantly outweigh the weaknesses mentioned, thus forming a robust argument for acceptance (Yes) or rejection (No). Ensure your evaluation uses explicit references from the reviews to substantiate your reasoning, creating a well-rounded conclusion that encompasses core aspects of research significance, clarity, and essential improvements suggested by the reviewers." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 50 0.71
        '# Task\n"Conduct a thorough evaluation of the research paper reviews, emphasizing contributions to existing knowledge, methodological rigor, theoretical insights, and presentation quality. Analyze and synthesize feedback to present a cohesive recommendation for acceptance (Yes) or rejection (No), ensuring you highlight how significant strengths, based on explicit reviewer insights, outweigh any weaknesses. Your conclusion should be substantiated by detailed references to the reviews, advancing a nuanced judgement of the paper\'s impact and value within the broader scholarly discourse." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 51 0.705
        '# Task\n"Perform a comprehensive review of the provided peer evaluations of the research paper, with a concentrated focus on the originality of the contributions, methodological integrity, theoretical and practical significance, as well as clarity of communication. Identify how the strengths elucidated by reviewers substantiate a strong case for acceptance (Yes), evidencing the ability of the findings to significantly advance the knowledge in the respective field, while addressing any minor weaknesses with constructive suggestions for improvement. Ensure the final recommendation conveys a well-grounded judgement on the merit of the paper’s impact and relevance in ongoing scholarly discussions." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        # OPRO - 52 trial 
        '# Task\n"Given the following reviews (text), determine if a paper would be accepted (Yes) or not (No) by an academic conference."\n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:',
        ]
    

    args = {"task": "metareviewer", "data_dir": "data/", "prompts": "prompts/metareview.md", "model": "gpt-4o-mini", "eval_model": "gpt-4o-mini", "out": "results/eval-240/test_top_prompts.out", "max_threads": 8, "temperature": 0.0, "expansion_temperature": 0.7, "optimizer": "nl-gradient", "rounds": 8, "beam_size": 5, "minibatch_size": 64, "n_gradients": 4, "errors_per_gradient": 4, "gradients_per_error": 1, "steps_per_gradient": 1, "mc_samples_per_step": 2, "max_expansion_factor": 8, "engine": "chatgpt", "evaluator": "bf", "scorer": "01", "eval_rounds": 5, "eval_prompts_per_round": 6, "samples_per_eval": 8, "c": 2.0, "knn_k": 2, "knn_t": 0.993, "reject_on_errors": False, "eval_budget": 240}
    # Print all candidates

    with open(args["out"], 'a') as outf:
        outf.write(f'args: {args}\n')

    # List of test files to evaluate
    test_files = [
        # 0
        'data/additional_data_800+800_2023_NeurIPS.csv',
        # 1
        'data/additional_data_800+800_2024_NeurIPS.csv',
        # 2
        'data/additional_data_800+800_2024_ICLR.csv',   
        # 3
        'data/additional_data_800+800_2025_ICLR.csv',
        # 4
        "data/metareviewer_data_train_800.csv",
        # 5
        'data/100+100_neurips_2024_train.csv',
        # 6
        "data/metareviewer_data_test_200.csv",
        # 7
        'data/25+100_neurips_2024_test.csv',
        # 8
        'data/100+25_neurips_2024_test.csv',
        # 9
        'data/100+100_neurips_2023_test.csv',
        # 10
        'data/40+160_neurips_2023_test.csv',
        # 11
        'data/160+40_neurips_2023_test.csv',
        # 12
        'data/178+22_neurips_2023_test.csv',
        # 13
        'data/33+100_neurips_2024_test.csv',
        # 14
        "data/140+140_neurips_2024_test.csv",
        # 15
        "data/50+150_neurips_2024_test.csv",
        # 16
        "data/50+150_neurips_2024_test_1.csv",
        # 17
        "data/50+150_neurips_2024_test_2.csv",
        # 18
        "data/50+150_neurips_2024_test_0_opro.csv",
        # 19
        "data/50+150_neurips_2024_test_1_opro.csv",
        # 20
        "data/50+150_neurips_2024_test_2_opro.csv",
        # 21
        "data/33+100_neurips_2024_test_1v1_0.csv",
        # 22
        "data/33+100_neurips_2024_test_1v1_1.csv",
        # 23
        "data/33+100_neurips_2024_test_1v1_2.csv",
    ]

    # pick candidate index 6, 7,8 for 140+140_neurips_2024_test.csv
    candidate_indices = [52]
    test_files_indices = [6]  
    splits = ['1v1', '8v1','4v1']

    task = tasks.MetareviewerBinaryTask('data/', 8)
    gpt4 = predictors.BinaryPredictor(args)

    for test_file_index in test_files_indices:
        test_file = test_files[test_file_index]
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

                accuracy = accuracy_score(sub_labels, sub_preds)
                micro_f1 = f1_score(sub_labels, sub_preds, average='micro')
                binary_f1 = f1_score(sub_labels, sub_preds, average='binary')
                macro_f1 = f1_score(sub_labels, sub_preds, average='macro')
                print(f"\ncnt_preds: {len(sub_preds)}\n - f1_micro: {micro_f1}\n - accuracy: {accuracy}\n - f1_binary: {binary_f1}\n - f1_macro: {macro_f1}\n")
                # f1s.append(f1)
                
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
                plt.savefig(f'results/cm/APRO_cm_train_{splits[1]}_candidate_{j}_test_{test_file_index}.png')
                plt.close()
                
            # with open(args["out"], 'a') as outf:  
            #     outf.write(f'{f1s}\n')
            print(f1s)
            
