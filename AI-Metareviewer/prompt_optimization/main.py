import requests
import os
import evaluators
import concurrent.futures
from tqdm import tqdm
import time
import json
import argparse
import scorers
import tasks
import predictors
import optimizers
from sklearn.metrics import f1_score

def get_task_class(task_name):
    if task_name == 'ethos':
        return tasks.EthosBinaryTask
    elif task_name == 'jailbreak':
        return tasks.JailbreakBinaryTask
    elif task_name == 'liar':
        return tasks.DefaultHFBinaryTask
    elif task_name == 'ar_sarcasm':
        return tasks.DefaultHFBinaryTask
    elif task_name == 'metareviewer':
        return tasks.MetareviewerBinaryTask
    else:
        raise Exception(f'Unsupported task: {task_name}')


def get_evaluator(evaluator):
    if evaluator == 'bf':
        return evaluators.BruteForceEvaluator
    elif evaluator in {'ucb', 'ucb-e'}:
        return evaluators.UCBBanditEvaluator
    elif evaluator in {'sr', 's-sr'}:
        return evaluators.SuccessiveRejectsEvaluator
    elif evaluator == 'sh':
        return evaluators.SuccessiveHalvingEvaluator
    else:
        raise Exception(f'Unsupported evaluator: {evaluator}')



def get_scorer(scorer):
    if scorer == '01':
        return scorers.Cached01Scorer
    elif scorer == 'll':
        return scorers.CachedLogLikelihoodScorer
    else:
        raise Exception(f'Unsupported scorer: {scorer}')


def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--task', default='metareviewer')
    parser.add_argument('--data_dir', default='data/')
    parser.add_argument('--prompts', default='prompts/metareview.md')
    parser.add_argument('--model', default='gpt-4o-mini')
    
    # parser.add_argument('--config', default='default.json')
    parser.add_argument('--out', default='results/41-nano/eval_test-ppt03.out')
    parser.add_argument('--max_threads', default=8, type=int)
    parser.add_argument('--temperature', default=0.0, type=float)
    parser.add_argument('--expansion_temperature', default=0.7, type=float)
    parser.add_argument('--optimizer', default='nl-gradient')

    # rounds
    parser.add_argument('--rounds', default=12, type=int)
    parser.add_argument('--beam_size', default=5, type=int)
    parser.add_argument('--n_test_exs', default=200, type=int) 
    parser.add_argument('--minibatch_size', default=64, type=int)

    # expansion parameters
    parser.add_argument('--n_gradients', default=6, type=int)   
    parser.add_argument('--errors_per_gradient', default=6, type=int)
    parser.add_argument('--gradients_per_error', default=1, type=int)
    parser.add_argument('--steps_per_gradient', default=1, type=int)
    parser.add_argument('--mc_samples_per_step', default=2, type=int)
    parser.add_argument('--max_expansion_factor', default=6, type=int)

    parser.add_argument('--engine', default="chatgpt", type=str)
    parser.add_argument('--evaluator', default="bf", type=str)
    parser.add_argument('--scorer', default="01", type=str)

    # selection parameters
    # optimization steps
    parser.add_argument('--eval_rounds', default=5, type=int)
    parser.add_argument('--eval_prompts_per_round', default=6, type=int)
    parser.add_argument('--samples_per_eval', default=8, type=int)
    parser.add_argument('--c', default=2.0, type=float, help='exploration param for UCB. higher = more exploration')

    parser.add_argument('--knn_k', default=2, type=int)
    parser.add_argument('--knn_t', default=0.993, type=float)
    parser.add_argument('--reject_on_errors', action='store_true') 
    
    args = parser.parse_args()

    return args


if __name__ == '__main__':
    args = get_args()

    config = vars(args) 

    config['eval_budget'] = config['samples_per_eval'] * config['eval_rounds'] * config['eval_prompts_per_round']
    
    task = get_task_class(args.task)(args.data_dir, args.max_threads)
    scorer = get_scorer(args.scorer)()
    evaluator = get_evaluator(args.evaluator)(config)
    bf_eval = get_evaluator('bf')(config)
    gpt4 = predictors.BinaryPredictor(config)
    # gpt4_ds = predictors.DeepSeekPredictor(config)

    optimizer = optimizers.ProTeGi(
        config, evaluator, scorer, args.max_threads, bf_eval)
    

    # all balanced
    train_exs = task.get_train_examples(config['data_dir'] + '/metareviewer_data_train_800.csv')
    test_exs = task.get_test_examples(config['data_dir'] + '/metareviewer_data_test_200.csv')

    if os.path.exists(args.out):
        os.remove(args.out)

    print(config)

    with open(args.out, 'a') as outf:
        outf.write(json.dumps(config) + '\n')
    
    candidates = [open(fp.strip()).read() for fp in args.prompts.split(',')]

    # candidates = ["# Task\nPlease analyze the attached evaluations of the academic paper to assess the overall sentiment and collective viewpoint regarding its potential acceptance at an academic conference. In your analysis, please concentrate on the following aspects:\n\n1. **Overall Sentiment**: Identify the predominant sentiment evident in the reviews. Assess whether the general feedback is more inclined towards acceptance or rejection, and note any key themes or elements that contribute to this sentiment. Consider both qualitative comments and numerical ratings.\n\n2. **Strengths and Weaknesses**: Compile a comprehensive list of the strengths and weaknesses identified by the reviewers. Clarify how these factors impact the overall perception of the paper. Pay particular attention to points of agreement among multiple reviewers, as these may indicate a stronger consensus.\n\n3. **Discrepancies in Ratings and Comments**: Examine the relationship between the reviewers' qualitative feedback and their numerical assessments. Look for cases where a rating seems inconsistent with the accompanying comments, and discuss how these discrepancies might affect the overall evaluation of the paper.\n\n4. **Confidence Level of Reviewers**: Assess the degree of confidence expressed by the reviewers in their evaluations. Consider how their level of confidence could influence the weight of their opinions. A consensus among reviewers who demonstrate high confidence may suggest a stronger likelihood for acceptance or rejection.\n\n5. **Conclusive Evaluation**: Based on your analysis, provide a reasoned prediction regarding the paper's prospects for acceptance or rejection. Support your prediction by summarizing the key insights from the reviews, focusing on sentiment trends and addressing any differing opinions among the reviewers.\n\nEnsure that your conclusion reflects the nuanced characteristics of both qualitative and quantitative feedback from the reviewers, taking into account individual perspectives as well as the collective consensus. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", "# Task\nAnalyze the attached evaluations of the academic paper to determine the prevailing sentiment and collective perspective regarding its potential for acceptance at an academic conference. In your analysis, please concentrate on the following aspects:\n\n1. **Overall Sentiment**: Identify the predominant sentiment expressed in the reviews. Assess whether the feedback tends to favor acceptance or rejection, and underline any key themes or factors influencing this sentiment. Consider both qualitative comments and numerical ratings.\n\n2. **Strengths and Limitations**: Compile a comprehensive list of the strengths and limitations highlighted by the reviewers. Clarify how these factors affect the overall perception of the paper. Pay close attention to areas where multiple reviewers agree, as this may indicate a stronger consensus.\n\n3. **Discrepancies in Ratings and Comments**: Analyze the relationship between the qualitative feedback provided by the reviewers and their numerical ratings. Look for instances where the scores seem at odds with the comments, and discuss how these discrepancies might influence the overall evaluation of the paper.\n\n4. **Reviewer Confidence**: Assess the degree of assurance expressed by the reviewers in their critiques. Consider how their level of confidence could impact the weight of their opinions. A consensus among reviewers who exhibit high confidence may suggest a higher probability of either acceptance or rejection.\n\n5. **Final Conclusion**: Based on your analysis, provide a well-reasoned prediction regarding the likelihood of the paper's acceptance or rejection. Back up your prediction with a summary of the essential findings from the reviews, focusing on sentiment trends and noting any differing opinions among the reviewers.\n\nEnsure that your conclusion captures the nuanced nature of both qualitative and quantitative feedback from the reviewers, taking into account individual perspectives as well as the broader consensus. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", '# Task\n"Review the commentary provided on the manuscript and deliver a thorough evaluation of its prospects for being accepted at an academic conference. Your analysis should encompass:\n\n1. **General Sentiment**: Assess the reviewers\' overall attitude towards the manuscript. Are their opinions mainly favorable or unfavorable? Highlight any major areas of disagreement or consensus among the reviewers.\n\n2. **Major Strengths and Weaknesses**: Provide a summary of the key strengths and weaknesses highlighted by the reviewers. Pay particular attention to points raised by multiple reviewers, as these signify the common agreement on the paper’s advantages and disadvantages.\n\n3. **Assessment of Limitations**: Evaluate the limitations pointed out in the reviews. Discuss whether the authors have adequately addressed these issues and how they might influence the reviewers’ ultimate recommendations.\n\n4. **Alignment of Ratings with Commentary**: Analyze the numerical ratings assigned by the reviewers in conjunction with their written feedback. Do the scores align with the qualitative observations? Identify any inconsistencies that might impact the acceptance decision.\n\n5. **Concluding Recommendation**: Finish with a recommendation regarding the likelihood of the manuscript’s acceptance. Make sure your assessment takes into account the overall sentiment, the main strengths and weaknesses, limitations, and how well the ratings correspond to the reviewers\' feedback.\n\nStrive for an unbiased evaluation that acknowledges both the favorable and unfavorable elements of the manuscript while offering clear and well-reasoned insights." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', "# Task\nReview the feedback received for the paper and evaluate the general sentiment regarding its likelihood of acceptance at an academic conference. Your assessment should encompass:\n\n1. **Reviewer Agreement**: Analyze the extent of consensus or disagreement among reviewers concerning the paper's merits and flaws. Identify any significant areas of agreement and discuss their influence on the overall acceptance sentiment.\n\n2. **Overall Sentiment**: Review the general sentiment expressed in the feedback, weighing both positive and negative comments. Are the reviewers generally leaning towards acceptance or rejection? Highlight notable opposing opinions and evaluate which sentiment appears to dominate.\n\n3. **Principal Strengths and Weaknesses**: Outline the key strengths and weaknesses noted by the reviewers, concentrating on points where there is broad agreement. This will illuminate the aspects of the paper that appeal to or detract from the reviewers’ perspectives.\n\n4. **Quantitative and Qualitative Analysis**: Examine how qualitative feedback correlates with numerical scores. Are there any inconsistencies between the reviewers' written comments and the ratings they provided? Document any gaps that might impact the acceptance outcome.\n\n5. **Reviewer Confidence**: Assess the confidence levels of the reviewers regarding their evaluations. How does their confidence align with the predominant opinion, and are there any confident dissenters offering differing views that could sway the overall sentiment?\n\n6. **Conclusive Recommendation**: Deliver a final recommendation concerning the paper's chances of acceptance, drawing from your analysis of the overall sentiment, noted strengths and weaknesses, rating discrepancies, and the reviewers’ confidence levels.\n\nMake sure your response presents a balanced perspective, acknowledging both the positive and negative elements of the paper while clearly expressing any areas of consensus or disagreement among the reviewers. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", "# Task\nReview the feedback provided for the academic paper in question and assess its overall sentiment along with the likelihood of acceptance at an academic conference. In your evaluation, consider the following elements:\n\n1. **Review Summaries**: Summarize each review, focusing on the main strengths and weaknesses identified by the reviewers. Identify any common themes and shared opinions that emerge across different evaluations.\n\n2. **Sentiment Analysis**: Analyze the general sentiment indicated by both the numerical ratings and the written feedback. Evaluate the balance of positive versus negative remarks, and identify any discrepancies or notable differences among the reviewers' perspectives.\n\n3. **Impact of Critiques**: Examine any major critiques, especially those from reviewers who express a high degree of confidence in their assessments, and evaluate how these criticisms could affect the paper's chances of acceptance. Even if some feedback is favorable, significant objections from others should be weighed heavily in your analysis.\n\n4. **Contextual Factors**: Consider the backdrop of the reviews, such as the confidence levels shown by each reviewer and any specific limitations they pointed out. How might these contextual elements affect the validity of their assessments?\n\n5. **Conclusive Assessment**: Develop a well-rounded conclusion regarding the chances of acceptance or rejection. Base your conclusion on evidence drawn from reviewers' comments and their combined insights, clearly explaining how the identified strengths and weaknesses interact to inform your final judgment.\n\nYour analysis should demonstrate a sophisticated understanding of the reviews, exploring the complex interplay between positive and negative feedback. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:"]
  
    for round in tqdm(range(config['rounds'])):
        print("STARTING ROUND ", round + 1)
        start = time.time()

        candidates = optimizer.expand_candidates(candidates, task, gpt4, train_exs)

        print(f"candidate counts : {len(candidates)}")

        scores = optimizer.score_candidates(candidates, task, gpt4, train_exs)
        [scores, candidates] = list(zip(*sorted(list(zip(scores, candidates)), reverse=True)))

        # select candidates
        candidates = candidates[:config['beam_size']]
        scores = scores[:config['beam_size']]

        # record candidates, estimated scores, and true scores
        with open(args.out, 'a') as outf:
            outf.write(f"======== ROUND {round + 1}\n")
            outf.write(f'{time.time() - start}\n')
            outf.write(f'{candidates}\n')
            outf.write(f'{[float(score) for score in scores]}\n')
            # outf.write('f1\n')

        metrics = []
        for candidate, score in zip(candidates, scores):
            if (args.n_test_exs > 100):
                labels_total = []
                preds_total = []
                for i in range(args.n_test_exs // 50):
                    _, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs[i * 50 : (i + 1) * 50], n=50)
                    labels_total.extend(labels)
                    preds_total.extend(preds)
                f1 = f1_score(labels_total, preds_total, average='micro')
                print(f"len(preds): {len(preds_total)}")
            else:
                _, f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs, n=args.n_test_exs)   
                print(f"len(preds): {len(preds)}")
            metrics.append(f1)
            with open(args.out, 'a') as outf:  
                outf.write(f'{metrics}\n')

    print("DONE!")
