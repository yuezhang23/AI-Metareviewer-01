import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns

# Read the data
data = pd.read_csv('results/compute_processed.out', sep='\t')

# Add method labels (first 10 prompts from method 1, rest from method 2)
data['method'] = ['Method 1'] * 10 + ['Method 2'] * 10
data['prompt_id'] = range(1, 21)

# Calculate bias (difference between f1-0 and f1-1)
data['bias'] = data['binary-f1-0'] - data['binary-f1-1']
data['abs_bias'] = abs(data['bias'])

# Set up the plotting style
plt.style.use('default')
sns.set_palette("husl")

# Create a figure with multiple subplots
fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, figsize=(15, 12))

# 1. Scatter plot: binary-f1-0 vs binary-f1-1 colored by method
for method in ['Method 1', 'Method 2']:
    subset = data[data['method'] == method]
    ax1.scatter(subset['binary-f1-0'], subset['binary-f1-1'], 
               label=method, alpha=0.7, s=100)
    
    # Add prompt IDs as annotations
    for idx, row in subset.iterrows():
        ax1.annotate(f"P{row['prompt_id']}", 
                    (row['binary-f1-0'], row['binary-f1-1']),
                    xytext=(5, 5), textcoords='offset points', fontsize=8)

ax1.plot([0, 1], [0, 1], 'k--', alpha=0.5, label='No bias line')
ax1.set_xlabel('Binary F1 Score (Class 0)')
ax1.set_ylabel('Binary F1 Score (Class 1)')
ax1.set_title('F1 Scores Comparison by Method')
ax1.legend()
ax1.grid(True, alpha=0.3)

# 2. Bias distribution by method
methods = ['Method 1', 'Method 2']
bias_data = [data[data['method'] == method]['bias'].values for method in methods]

bp = ax2.boxplot(bias_data, labels=methods, patch_artist=True)
colors = ['lightblue', 'lightcoral']
for patch, color in zip(bp['boxes'], colors):
    patch.set_facecolor(color)

ax2.axhline(y=0, color='red', linestyle='--', alpha=0.7, label='No bias')
ax2.set_ylabel('Bias (F1-0 - F1-1)')
ax2.set_title('Bias Distribution by Method')
ax2.legend()
ax2.grid(True, alpha=0.3)

# 3. Individual prompt bias comparison
x_pos = np.arange(len(data))
colors = ['lightblue' if m == 'Method 1' else 'lightcoral' for m in data['method']]

bars = ax3.bar(x_pos, data['bias'], color=colors, alpha=0.7)
ax3.axhline(y=0, color='red', linestyle='--', alpha=0.7)
ax3.set_xlabel('Prompt ID')
ax3.set_ylabel('Bias (F1-0 - F1-1)')
ax3.set_title('Individual Prompt Bias')
ax3.set_xticks(x_pos[::2])  # Show every other prompt ID
ax3.set_xticklabels([f"P{i}" for i in data['prompt_id'][::2]])
ax3.grid(True, alpha=0.3)

# Add legend for methods
from matplotlib.patches import Patch
legend_elements = [Patch(facecolor='lightblue', label='Method 1'),
                   Patch(facecolor='lightcoral', label='Method 2')]
ax3.legend(handles=legend_elements)

# 4. Summary statistics
method_stats = data.groupby('method').agg({
    'bias': ['mean', 'std', 'min', 'max'],
    'abs_bias': 'mean'
}).round(3)

# Create a table
table_data = []
for method in methods:
    stats = method_stats.loc[method]
    table_data.append([
        method,
        f"{stats[('bias', 'mean')]:.3f}",
        f"{stats[('bias', 'std')]:.3f}",
        f"{stats[('bias', 'min')]:.3f}",
        f"{stats[('bias', 'max')]:.3f}",
        f"{stats[('abs_bias', 'mean')]:.3f}"
    ])

ax4.axis('tight')
ax4.axis('off')
table = ax4.table(cellText=table_data,
                  colLabels=['Method', 'Mean Bias', 'Std Bias', 'Min Bias', 'Max Bias', 'Mean Abs Bias'],
                  cellLoc='center',
                  loc='center')
table.auto_set_font_size(False)
table.set_fontsize(10)
table.scale(1.2, 1.5)
ax4.set_title('Bias Statistics by Method', pad=20)

plt.tight_layout()
plt.savefig('results/bias_comparison.png', dpi=300, bbox_inches='tight')
plt.show()

# Print summary statistics
print("\n=== BIAS ANALYSIS SUMMARY ===")
print(f"Method 1 (Prompts 1-10):")
print(f"  Mean bias: {data[data['method'] == 'Method 1']['bias'].mean():.3f}")
print(f"  Std bias: {data[data['method'] == 'Method 1']['bias'].std():.3f}")
print(f"  Mean absolute bias: {data[data['method'] == 'Method 1']['abs_bias'].mean():.3f}")

print(f"\nMethod 2 (Prompts 11-20):")
print(f"  Mean bias: {data[data['method'] == 'Method 2']['bias'].mean():.3f}")
print(f"  Std bias: {data[data['method'] == 'Method 2']['bias'].std():.3f}")
print(f"  Mean absolute bias: {data[data['method'] == 'Method 2']['abs_bias'].mean():.3f}")

# Statistical test for bias difference
from scipy import stats
method1_bias = data[data['method'] == 'Method 1']['bias']
method2_bias = data[data['method'] == 'Method 2']['bias']

t_stat, p_value = stats.ttest_ind(method1_bias, method2_bias)
print(f"\nStatistical Test (t-test):")
print(f"  t-statistic: {t_stat:.3f}")
print(f"  p-value: {p_value:.3f}")
print(f"  Significant difference: {'Yes' if p_value < 0.05 else 'No'} (α=0.05)")

# Save detailed results
data.to_csv('results/bias_analysis_results.csv', index=False)
print(f"\nDetailed results saved to: results/bias_analysis_results.csv") 