import tasks
import predictors
from tqdm import tqdm
import json
import ast
import scorers
import evaluators
import re
import argparse

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

def test_eval(file_name, threads=16):
    # Read the file
    with open(file_name, 'r') as f:
        config_line = f.readline()  
        config = json.loads(config_line)
        lines = f.readlines()

    round_info = find_round_positions(lines)

    task = tasks.MetareviewerBinaryTask('data/', threads)
    gpt4 = predictors.BinaryPredictor(config)
    test_exs = task.get_test_examples()

    new_lines = lines.copy()

    for round_data in round_info:
        print(f"Processing Round {round_data['round']}")
        
        try:
            # Get candidates for this round
            candidates_line = lines[round_data['candidates_line']]
            candidates = ast.literal_eval(candidates_line.strip())
            
            # Initialize metrics list with None values
            metrics = [None] * len(candidates)
            
            # Evaluate candidates
            for i, candidate in enumerate(tqdm(candidates, desc=f"Evaluating candidates for Round {round_data['round']}")):
                try:
                    f1, texts, labels, preds = task.evaluate(
                        gpt4, 
                        candidate, 
                        test_exs, 
                        n=config['n_test_exs']
                    )
                    metrics[i] = f1
                    print(f"Candidate {i+1}/{len(candidates)} evaluated with F1: {f1}")
                    
                    # Update the metrics in the file after each evaluation
                    f1_line_position = round_data['f1_line']
                    new_lines[f1_line_position] = f"[{', '.join(str(m) if m is not None else 'None' for m in metrics)}]\n"
                    
                    # Write the updated content back to the file
                    with open(file_name, 'w') as f:
                        f.writelines([config_line] + new_lines)
                    
                except Exception as e:
                    print(f"Error evaluating candidate {i+1}: {str(e)}")
                    metrics[i] = None

                    f1_line_position = round_data['f1_line']
                    new_lines[f1_line_position] = f"[{', '.join(str(m) if m is not None else 'None' for m in metrics)}]\n"
                    with open(file_name, 'w') as f:
                        f.writelines([config_line] + new_lines)
            
            print(f"Completed Round {round_data['round']}")
            
        except Exception as e:
            print(f"Error processing Round {round_data['round']}: {str(e)}")
            continue

    print("Evaluation completed!")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Run inference evaluation on a specified file')
    parser.add_argument('--file_name', type=str, required=True, help='Path to the file to evaluate')
    parser.add_argument('--max_threads', type=int, default=16, help='Maximum number of threads to use')
    args = parser.parse_args()
    
    test_eval(args.file_name, args.max_threads)