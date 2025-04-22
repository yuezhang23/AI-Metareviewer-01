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

            candidates_line = i + 2
            if lines[i + 4].startswith('f1'):
                f1_line = i + 4
            else:
                f1_line = None

            if candidates_line and f1_line:
                round_info.append({
                    'round': round_num,
                    'round_start': i,
                    'candidates_line': candidates_line,
                    'f1_line': f1_line
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
task = tasks.MetareviewerBinaryTask('data/', 20)
gpt4 = predictors.BinaryPredictor(config)
test_exs = task.get_test_examples()

# Process each round
new_lines = lines.copy()

for round_data in round_info:
    print(f"Processing Round {round_data['round']}")
    
    try:
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
        
        # Replace the f1 line with computed metrics
        f1_line_position = round_data['f1_line']
        new_lines[f1_line_position] = f"[{', '.join(map(str, metrics))}]\n"
        
        
        # Write the updated content back to the file after each round
        with open('results/meta_review.out', 'w') as f:
            f.writelines([config_line] + new_lines)
        
        print(f"Saved results for Round {round_data['round']}")
        
    except Exception as e:
        print(f"Error processing Round {round_data['round']}: {str(e)}")
        continue

print("Evaluation completed!")