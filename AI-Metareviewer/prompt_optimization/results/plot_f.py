import re
import matplotlib.pyplot as plt
import argparse

def extract_f1_scores(file_path):
    rounds = []
    max_f1_scores = []
    mean_f1_scores = []
    
    with open(file_path, 'r') as file:
        lines = file.readlines()
    
    for i, line in enumerate(lines):
        if '======== ROUND' in line:
            # Extract round number
            round_num = int(line.split('ROUND')[1].strip())
            
            # Get F1 scores from 4 lines below
            f1_line = lines[i + 4]
            # Extract scores between brackets
            scores_str = f1_line.split('[')[1].split(']')[0]
            scores = [float(x) for x in scores_str.split(',')]
            # Get max and mean F1 score for this round
            max_f1 = max(scores)
            mean_f1 = sum(scores) / len(scores)
            rounds.append(round_num)
            max_f1_scores.append(max_f1)
            mean_f1_scores.append(mean_f1)
    
    return rounds, max_f1_scores, mean_f1_scores

def plot_f1_scores(results):
    plt.figure(figsize=(12, 6))
    
    # Define colors for each label
    colors = ['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728', '#9467bd', '#8c564b']
    
    # Plot for each file
    for i, (label, (rounds, max_f1_scores, mean_f1_scores)) in enumerate(results.items()):
        color = colors[i % len(colors)]
        # Plot max F1 scores
        plt.plot(rounds, max_f1_scores, marker='o', linestyle='-', linewidth=2, 
                label=f'{label} Max F1', color=color)
        # Plot mean F1 scores
        plt.plot(rounds, mean_f1_scores, marker='s', linestyle='--', linewidth=2, 
                label=f'{label} Mean F1', color=color)
        
        # Add value labels on top of each point
        for i, (max_score, mean_score) in enumerate(zip(max_f1_scores, mean_f1_scores)):
            plt.text(rounds[i], max_score, f'{max_score:.3f}', ha='center', va='bottom')
            plt.text(rounds[i], mean_score, f'{mean_score:.3f}', ha='center', va='top')
    
    plt.xlabel('Round')
    plt.ylabel('F1 Score')
    plt.title('Test F1 Scores - UCB vs BF')
    plt.grid(True)
    plt.xlim(0, 17)
    plt.xticks(rounds)  # Using rounds from the last file (they should be the same)
    plt.ylim(0.58, 0.75)  # Set y-axis limits based on observed F1 scores
    plt.legend()
    
    plt.savefig('results/graphs/f1_scores_plot-eval-400.png')
    plt.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Plot F1 scores from test results')
    parser.add_argument('--files', nargs='+', type=str, help='Paths to the test results files')
    parser.add_argument('--labels', nargs='+', type=str, help='Labels for each file')
    args = parser.parse_args()
    
    if len(args.files) != len(args.labels):
        raise ValueError("Number of files must match number of labels")
    
    results = {}
    for file_path, label in zip(args.files, args.labels):
        rounds, max_f1_scores, mean_f1_scores = extract_f1_scores(file_path)
        results[label] = (rounds, max_f1_scores, mean_f1_scores)
    
    plot_f1_scores(results)