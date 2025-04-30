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
            if i + 4 < len(lines):
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

def plot_f1_scores(rounds, max_f1_scores, mean_f1_scores):
    plt.figure(figsize=(12, 6))
    
    # Plot max F1 scores
    plt.plot(rounds, max_f1_scores, marker='o', linestyle='-', linewidth=2, label='Max F1 Score')
    # Plot mean F1 scores
    plt.plot(rounds, mean_f1_scores, marker='s', linestyle='--', linewidth=2, label='Mean F1 Score')
    
    plt.xlabel('Round')
    plt.ylabel('F1 Score')
    plt.title('Test F1 Scores Across Rounds  - Eval-Budget: 360')
    plt.grid(True)
    plt.xticks(rounds)
    plt.ylim(0.5, 1.0)  # Set y-axis limits based on observed F1 scores
    plt.legend()
    
    # Add value labels on top of each point
    for i, (max_score, mean_score) in enumerate(zip(max_f1_scores, mean_f1_scores)):
        plt.text(rounds[i], max_score, f'{max_score:.3f}', ha='center', va='bottom')
        plt.text(rounds[i], mean_score, f'{mean_score:.3f}', ha='center', va='top')
    
    plt.savefig('results/f1_scores_plot.png')
    plt.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Plot F1 scores from test results')
    parser.add_argument('--path', type=str, help='Path to the test results file')
    args = parser.parse_args()
    
    rounds, max_f1_scores, mean_f1_scores = extract_f1_scores(args.path)
    plot_f1_scores(rounds, max_f1_scores, mean_f1_scores)