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
    # parser.add_argument('--config', default='default.json')
    parser.add_argument('--out', default='results/test.out')
    parser.add_argument('--max_threads', default=6, type=int)
    parser.add_argument('--temperature', default=0.0, type=float)
    parser.add_argument('--expansion_temperature', default=0.6, type=float)
    parser.add_argument('--optimizer', default='nl-gradient')

    # rounds
    parser.add_argument('--rounds', default=3, type=int)
    parser.add_argument('--beam_size', default=4, type=int)
    parser.add_argument('--n_test_exs', default=200, type=int) 
    parser.add_argument('--minibatch_size', default=64, type=int)

    # expansion parameters
    parser.add_argument('--n_gradients', default=4, type=int)   
    parser.add_argument('--errors_per_gradient', default=4, type=int)
    parser.add_argument('--gradients_per_error', default=1, type=int)
    parser.add_argument('--steps_per_gradient', default=1, type=int)
    parser.add_argument('--mc_samples_per_step', default=2, type=int)
    parser.add_argument('--max_expansion_factor', default=8, type=int)

    parser.add_argument('--engine', default="chatgpt", type=str)
    parser.add_argument('--evaluator', default="bf", type=str)
    parser.add_argument('--scorer', default="01", type=str)

    # selection parameters
    # optimization steps
    parser.add_argument('--eval_rounds', default=3, type=int)
    parser.add_argument('--eval_prompts_per_round', default=4, type=int)
    parser.add_argument('--samples_per_eval', default=3, type=int)
    parser.add_argument('--c', default=1.5, type=float, help='exploration param for UCB. higher = more exploration')

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

    optimizer = optimizers.ProTeGi(
        config, evaluator, scorer, args.max_threads, bf_eval)

    train_exs = task.get_train_examples(config['data_dir'] + '/metareviewer_data_train_200.csv')
    test_exs = task.get_test_examples(config['data_dir'] + '/metareviewer_data_test_200.csv')

    if os.path.exists(args.out):
        os.remove(args.out)

    print(config)

    with open(args.out, 'a') as outf:
        outf.write(json.dumps(config) + '\n')
    
    candidates = [open(fp.strip()).read() for fp in args.prompts.split(',')]

    # candidates = ["# Task\nAnalyze the sentiment expressed in the given reviews for the research paper, concentrating on the general evaluation regarding its chances of being accepted, rejected, or requiring minor revisions. Take into account the reviewers' identification of strengths and weaknesses, as well as their ratings and levels of confidence. When evaluating sentiment, be mindful of the specific wording in the reviews, including indications of enthusiasm, apprehension, or doubt, and how these elements shape the overall perception of the paper's quality and likelihood of acceptance. Deliver a definitive conclusion about the sentiment classification based on these considerations. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:", '# Task\nBased on the reviews provided, assess whether the paper in question would be approved for presentation at an academic conference. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', '# Task\n"Analyze the reviews given for the paper, considering the strengths and weaknesses pointed out by the reviewers. Decide if the overall evaluation indicates that the paper should be approved or denied for presentation at an academic conference, factoring in the importance of the contributions, robustness of the methodology, clarity of presentation, and any identified shortcomings." \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:', '# Task\nBased on the provided reviews of the academic paper, analyze its potential for acceptance at a conference by focusing on several essential criteria. Present a clear and succinct evaluation that covers each aspect:\n\n1. **Soundness**: Examine the technical rigor of the research, taking into account the methodology and experimental framework.\n\n2. **Presentation**: Assess the clarity, organization, and overall readability of the paper, including the effectiveness of any visual aids.\n\n3. **Contribution**: Evaluate the originality and importance of the findings relative to the current body of literature.\n\n4. **Strengths and Weaknesses**: Highlight the key strengths that favor acceptance, along with specific weaknesses or limitations that may pose challenges to acceptance.\n\n5. **Overall Rating**: Provide a concluding judgment regarding the likelihood of the paper being accepted, rejected, or deemed borderline. Justify your rating based on the discussed criteria.\n\nPlease ensure your evaluation is direct and encapsulates the core elements of each review without unnecessary elaboration. \n\n# Output format\nAnswer Yes or No as labels\n\n# Prediction\nText: {{ text }}\nLabel:']
    
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
                    _, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs[i * 50 : (i + 1) * 50])
                    labels_total.extend(labels)
                    preds_total.extend(preds)
                f1 = f1_score(labels_total, preds_total, average='micro')
                metrics.append(f1)
            else:
                f1, texts, labels, preds = task.evaluate(gpt4, candidate, test_exs, n=args.n_test_exs)
                metrics.append(f1)
            with open(args.out, 'a') as outf:  
                outf.write(f'{metrics}\n')

    print("DONE on train set!")
