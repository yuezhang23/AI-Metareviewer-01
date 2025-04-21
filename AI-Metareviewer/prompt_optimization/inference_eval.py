import tasks
import predictors
from tqdm import tqdm
import json
import ast
import scorers
import evaluators
import re

def find_round_positions(lines):
    """Find the start positions of each round and their corresponding candidates"""
    round_info = []
    for i, line in enumerate(lines):
        if line.startswith('======== ROUND'):
            round_num = int(line.strip().split()[-1])
            # Find the candidates line and empty line before next round
            candidates_line = None
            empty_line = None
            for j in range(i + 1, len(lines)):
                if lines[j].startswith("('# Task"):
                    candidates_line = j
                elif j < len(lines) - 1 and lines[j].strip() == '' and lines[j+1].startswith('======== ROUND'):
                    empty_line = j
                    break
                elif j < len(lines) - 1 and lines[j+1].startswith('======== ROUND'):
                    # No empty line found before next round
                    break
            
            if candidates_line:
                round_info.append({
                    'round': round_num,
                    'round_start': i,
                    'candidates_line': candidates_line,
                    'empty_line': empty_line
                })
    
    return round_info

# Read the file
with open('results/meta_review.out', 'r') as f:
    config_line = f.readline()
    config = json.loads(config_line)
    lines = f.readlines()

# Get round information
round_info = find_round_positions(lines)

# Initialize components
task = tasks.MetareviewerBinaryTask('data/', config['max_threads'])
gpt4 = predictors.BinaryPredictor(config)
test_exs = task.get_test_examples()

# Process each round and write results on the fly
for round_data in round_info:
    print(f"Processing Round {round_data['round']}")
    
    # Skip if no empty line found before next round
    if round_data['empty_line'] is None:
        continue
    
    # Get candidates for this round
    candidates_line = lines[round_data['candidates_line']]
    candidates = ast.literal_eval(candidates_line.strip())
    
    # Evaluate candidates
    metrics = []
    for candidate in tqdm(candidates, desc=f"Evaluating candidates for Round {round_data['round']}"):
        try:
            f1, texts, labels, preds = task.evaluate(
                gpt4, 
                candidate, 
                test_exs, 
                n=config['n_test_exs']
            )
            metrics.append(f1)
            print(f"Candidate evaluated with F1: {f1}")
        except Exception as e:
            print(f"Error evaluating candidate: {str(e)}")
            metrics.append(None)
    
    # Write the metrics to the empty line immediately
    lines[round_data['empty_line']] = f"[{', '.join(map(str, metrics))}]\n"
    
    # Write the current state back to file
    with open('results/meta_review.out', 'w') as f:
        f.writelines([config_line] + lines)
    
    print(f"Inserted metrics for Round {round_data['round']}: {metrics}")

print("Evaluation completed!")