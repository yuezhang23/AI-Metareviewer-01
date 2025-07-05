import re
import matplotlib.pyplot as plt

def extract_f1_scores(file_path):
    rounds = []
    f1_scores = []
    
    with open(file_path, 'r') as file:
        content = file.read()
        
    # Split content into rounds
    round_sections = content.split('======== ROUND')
    
    for section in round_sections[1:]:  # Skip the first empty section
        # Extract round number
        round_num = int(section.split('\n')[0].strip())
        
        # Find F1 scores line
        f1_line = re.search(r'F1 :\[(.*?)\]', section)
        if f1_line:
            # Convert string of scores to list of floats
            scores = [float(x) for x in f1_line.group(1).split(',')]
            # Get max F1 score for this round
            max_f1 = max(scores)
            rounds.append(round_num)
            f1_scores.append(max_f1)
    
    return rounds, f1_scores

def plot_f1_scores(rounds, f1_scores):
    plt.figure(figsize=(10, 6))
    plt.plot(rounds, f1_scores, marker='o', linestyle='-', linewidth=2)
    plt.xlabel('Round Number')
    plt.ylabel('Max F1 Score')
    plt.title('Max F1 Scores Across Rounds')
    plt.grid(True)
    plt.xticks(rounds)
    plt.ylim(0.5, 0.8)  # Set y-axis limits based on observed F1 scores
    
    # Add value labels on top of each point
    for i, score in enumerate(f1_scores):
        plt.text(rounds[i], score, f'{score:.3f}', ha='center', va='bottom')
    
    plt.savefig('f1_scores_plot.png')
    plt.close()

if __name__ == "__main__":
    file_path = "AI-Metareviewer/prompt_optimization/results/test.out"
    rounds, f1_scores = extract_f1_scores(file_path)
    plot_f1_scores(rounds, f1_scores) 