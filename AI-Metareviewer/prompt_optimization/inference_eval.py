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
            # Find the candidates line (starts with '(#Task')
            for j in range(i + 1, len(lines)):
                if lines[j].startswith("('# Task"):
                    round_info.append({
                        'round': round_num,
                        'round_start': i,
                        'candidates_line': j,
                        'next_round': None
                    })
                    break
    
    # Set the next_round position for each round
    for i in range(len(round_info) - 1):
        round_info[i]['next_round'] = round_info[i + 1]['round_start']
    
    return round_info

# Read the file
with open('results/meta_review.out', 'r') as f:
    config = json.loads(f.readline())  # First line contains the config
    lines = f.readlines()

# Get round information
round_info = find_round_positions(lines)

# Initialize components
task = tasks.MetareviewerBinaryTask('data/', config['max_threads'])
gpt4 = predictors.BinaryPredictor(config)
test_exs = task.get_test_examples()

# Process each round
new_lines = lines.copy()
lines_inserted = 0  # Track number of lines inserted to adjust positions

for round_data in round_info:
    print(f"Processing Round {round_data['round']}")
    
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
    
    # Find where to insert the metrics
    if round_data['next_round'] is not None:
        insert_position = round_data['next_round'] + lines_inserted
    else:
        insert_position = len(new_lines)  # Insert at the end if it's the last round
    
    # Insert the metrics line
    new_lines.insert(insert_position, f"[{', '.join(map(str, metrics))}]\n")
    lines_inserted += 1
    
    print(f"Inserted metrics for Round {round_data['round']}: {metrics}")

# Write the updated content back to the file
with open('results/meta_review.out', 'w') as f:
    f.writelines([config_line] + new_lines)

print("Evaluation completed!")