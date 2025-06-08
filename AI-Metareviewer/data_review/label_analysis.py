import pandas as pd
import os
from collections import defaultdict
import matplotlib.pyplot as plt
import seaborn as sns

def analyze_labels(csv_files):
    """
    Analyze label distributions across multiple CSV files.
    
    Args:
        csv_files (list): List of paths to CSV files to analyze
        
    Returns:
        dict: Dictionary containing label counts and statistics for each file
    """
    results = defaultdict(dict)
    
    for file_path in csv_files:
        # Read CSV file
        df = pd.read_csv(file_path, sep=';')
        
        # Get filename without extension
        file_name = os.path.basename(file_path).replace('.csv', '')
        
        # Count labels
        label_counts = df['label'].value_counts()
        total_samples = len(df)
        
        # Store results
        results[file_name] = {
            'total_samples': total_samples,
            'label_0_count': label_counts.get(0, 0),
            'label_1_count': label_counts.get(1, 0),
            'label_0_percentage': (label_counts.get(0, 0) / total_samples) * 100,
            'label_1_percentage': (label_counts.get(1, 0) / total_samples) * 100
        }
    
    return results

def print_summary(results):
    """Print a summary of the label analysis results."""
    print("\nLabel Distribution Summary:")
    print("-" * 80)
    print(f"{'File Name':<20} {'Total Samples':<15} {'Label 0':<15} {'Label 1':<15} {'% Label 0':<15} {'% Label 1':<15}")
    print("-" * 80)
    
    for file_name, stats in results.items():
        print(f"{file_name:<20} {stats['total_samples']:<15} {stats['label_0_count']:<15} "
              f"{stats['label_1_count']:<15} {stats['label_0_percentage']:.2f}%{'':<10} "
              f"{stats['label_1_percentage']:.2f}%{'':<10}")

def plot_distributions(results):
    """Create bar plots to visualize label distributions."""
    # Prepare data for plotting
    files = list(results.keys())
    label_0_percentages = [stats['label_0_percentage'] for stats in results.values()]
    label_1_percentages = [stats['label_1_percentage'] for stats in results.values()]
    
    # Create figure with two subplots
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))
    
    # Plot absolute counts
    x = range(len(files))
    width = 0.35
    ax1.bar([i - width/2 for i in x], [stats['label_0_count'] for stats in results.values()], 
            width, label='Label 0')
    ax1.bar([i + width/2 for i in x], [stats['label_1_count'] for stats in results.values()], 
            width, label='Label 1')
    ax1.set_ylabel('Count')
    ax1.set_title('Absolute Label Counts')
    ax1.set_xticks(x)
    ax1.set_xticklabels(files, rotation=45, ha='right')
    ax1.legend()
    
    # Plot percentages
    ax2.bar([i - width/2 for i in x], label_0_percentages, width, label='Label 0')
    ax2.bar([i + width/2 for i in x], label_1_percentages, width, label='Label 1')
    ax2.set_ylabel('Percentage')
    ax2.set_title('Label Percentages')
    ax2.set_xticks(x)
    ax2.set_xticklabels(files, rotation=45, ha='right')
    ax2.legend()
    
    plt.tight_layout()
    plt.savefig('label_distribution.png')
    plt.close()

def main():
    # Get all CSV files in the current directory
    current_dir = os.path.dirname(os.path.abspath(__file__))
    csv_files = [os.path.join(current_dir, f) for f in os.listdir(current_dir) 
                if f.endswith('.csv') and f != 'label_analysis.py']
    
    if not csv_files:
        print("No CSV files found in the directory!")
        return
    
    # Analyze labels
    results = analyze_labels(csv_files)
    
    # Print summary
    print_summary(results)
    
    # Create visualization
    plot_distributions(results)
    print("\nVisualization saved as 'label_distribution.png'")

if __name__ == "__main__":
    main() 