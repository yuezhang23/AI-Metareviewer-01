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
import optimizers_ds
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
    parser.add_argument('--eval_model', default='gpt-4o-mini')
    
    # parser.add_argument('--config', default='default.json')
    parser.add_argument('--out', default='results/01bf/eval-240/bf-mini-eval240-ppt03-tp0.7-bs5-48520.out')
    parser.add_argument('--max_threads', default=8, type=int)
    parser.add_argument('--temperature', default=0.0, type=float)
    parser.add_argument('--expansion_temperature', default=0.7, type=float)
    parser.add_argument('--optimizer', default='nl-gradient')

    # rounds
    parser.add_argument('--rounds', default=6, type=int)
    parser.add_argument('--beam_size', default=5, type=int)
    parser.add_argument('--n_test_exs', default=200, type=int) 
    parser.add_argument('--minibatch_size', default=64, type=int)

    # expansion parameters
    parser.add_argument('--n_gradients', default=4, type=int)   
    parser.add_argument('--errors_per_gradient', default=8, type=int)
    parser.add_argument('--gradients_per_error', default=5, type=int)
    parser.add_argument('--steps_per_gradient', default=2, type=int)
    parser.add_argument('--mc_samples_per_step', default=0, type=int)
    parser.add_argument('--max_expansion_factor', default=6, type=int)

    parser.add_argument('--engine', default="chatgpt", type=str)
    parser.add_argument('--evaluator', default="ucb", type=str)
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
    optimizer = optimizers.ProTeGi(
        config, evaluator, scorer, args.max_threads, bf_eval)
    
    # gpt4 = predictors.DeepSeekPredictor(config)
    # optimizer = optimizers_ds.ProTeGi_ds(
    #     config, evaluator, scorer, args.max_threads, bf_eval)


    # all balanced
    train_exs = task.get_train_examples(config['data_dir'] + '/metareviewer_data_train_800.csv')
    test_exs = task.get_test_examples(config['data_dir'] + '/metareviewer_data_test_200.csv')

    if os.path.exists(args.out):
        os.remove(args.out)

    print(config)

    with open(args.out, 'a') as outf:
        outf.write(json.dumps(config) + '\n')
    
    candidates = [open(fp.strip()).read() for fp in args.prompts.split(',')]

    for round in tqdm(range(config['rounds'])):
        print("STARTING ROUND ", round + 1)
        start = time.time()

        candidates = optimizer.expand_candidates(candidates, task, gpt4, train_exs)

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
                    _, f1, texts, labels, preds = task.evaluate(gpt4, candidate, args.eval_model, test_exs[i * 50 : (i + 1) * 50], n=50)
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
