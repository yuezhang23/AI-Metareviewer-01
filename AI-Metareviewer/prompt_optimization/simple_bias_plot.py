import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Read the data
data = pd.read_csv('results/compute_processed.out', sep='\t')

# Add method labels
data['method'] = ['Method 1'] * 10 + ['Method 2'] * 10
data['prompt_id'] = range(1, 21)

# Calculate bias
data['bias'] = data['binary-f1-0'] - data['binary-f1-1']

# Create the main bias comparison plot
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))

# Plot 1: Scatter plot with bias visualization
colors = ['red' if bias > 0 else 'blue' for bias in data['bias']]
sizes = [abs(bias) * 500 + 50 for bias in data['bias']]  # Size based on bias magnitude

for i, method in enumerate(['Method 1', 'Method 2']):
    subset = data[data['method'] == method]
    subset_colors = colors[10*i:10*(i+1)]
    subset_sizes = sizes[10*i:10*(i+1)]
    
    ax1.scatter(subset['binary-f1-0'], subset['binary-f1-1'], 
               c=subset_colors, s=subset_sizes, alpha=0.7, 
               label=method, edgecolors='black', linewidth=0.5)

# Add diagonal line for no bias
ax1.plot([0, 1], [0, 1], 'k--', alpha=0.5, label='No bias line')
ax1.set_xlabel('Binary F1 Score (Class 0)')
ax1.set_ylabel('Binary F1 Score (Class 1)')
ax1.set_title('F1 Scores with Bias Visualization\n(Red=Class 0 bias, Blue=Class 1 bias, Size=Bias magnitude)')
ax1.legend()
ax1.grid(True, alpha=0.3)

# Plot 2: Bias comparison by method
method1_bias = data[data['method'] == 'Method 1']['bias']
method2_bias = data[data['method'] == 'Method 2']['bias']

# Box plot
bp = ax2.boxplot([method1_bias, method2_bias], 
                 labels=['Method 1\n(Prompts 1-10)', 'Method 2\n(Prompts 11-20)'],
                 patch_artist=True)
bp['boxes'][0].set_facecolor('lightcoral')
bp['boxes'][1].set_facecolor('lightblue')

# Add individual points
ax2.scatter([1] * len(method1_bias), method1_bias, color='red', alpha=0.6, s=50)
ax2.scatter([2] * len(method2_bias), method2_bias, color='blue', alpha=0.6, s=50)

ax2.axhline(y=0, color='black', linestyle='--', alpha=0.7, label='No bias')
ax2.set_ylabel('Bias (F1-0 - F1-1)')
ax2.set_title('Bias Distribution by Method')
ax2.legend()
ax2.grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('results/simple_bias_comparison.png', dpi=300, bbox_inches='tight')
plt.show()

# Print summary
print("=== SIMPLE BIAS COMPARISON ===")
print(f"Method 1 bias: {method1_bias.mean():.3f} ± {method1_bias.std():.3f}")
print(f"Method 2 bias: {method2_bias.mean():.3f} ± {method2_bias.std():.3f}")
print(f"Method 1 |bias|: {abs(method1_bias).mean():.3f}")
print(f"Method 2 |bias|: {abs(method2_bias).mean():.3f}") 