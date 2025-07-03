import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Read the data
data = []
current_method = None

with open('results/compute_processed.out', 'r') as f:
    lines = f.readlines()

for line in lines:
    line = line.strip()
    if line == 'Initial_Prompts':
        current_method = 'Initial_Prompts'
    if line == 'APO':
        current_method = 'APO'
    elif line == 'OPRO':
        current_method = 'OPRO'
    elif line == 'binary-f1-0\tbinary-f1-1':
        continue
    elif line == 'Imbalanced Data':
        current_method = 'Imbalanced Data'
    elif line == 'Balanced Data':
        current_method = 'Balanced Data'
    elif line and current_method and '\t' in line:
        f1_0, f1_1 = map(float, line.split('\t'))
        data.append({
            'method': current_method,
            'binary-f1-0': f1_0,
            'binary-f1-1': f1_1,
            'bias': f1_0 - f1_1
        })

# Convert to DataFrame
df = pd.DataFrame(data)

# Create the visualization
plt.figure(figsize=(8, 8))

# Create single plot instead of subplots
fig, ax1 = plt.subplots(1, 1, figsize=(8, 8))

# Plot 1: Individual prompt performance comparison
apo_data = df[df['method'] == 'APO']
opro_data = df[df['method'] == 'OPRO']
initial_prompts_data = df[df['method'] == 'Initial_Prompts']
imbalanced_data_data = df[df['method'] == 'Imbalanced Data']
balanced_data_data = df[df['method'] == 'Balanced Data']

# Plot binary-f1-0 vs binary-f1-1 for each method
# ax1.scatter(apo_data['binary-f1-0'], apo_data['binary-f1-1'], 
#            color='blue', alpha=0.7, s=200, label='APO', edgecolors='black')
# ax1.scatter(opro_data['binary-f1-0'], opro_data['binary-f1-1'], 
#            color='red', alpha=0.7, s=200, label='OPRO', edgecolors='black')
ax1.scatter(initial_prompts_data['binary-f1-0'], initial_prompts_data['binary-f1-1'], 
           color='yellow', alpha=0.7, s=200, label='Initial Prompts', edgecolors='black')
ax1.scatter(imbalanced_data_data['binary-f1-0'], imbalanced_data_data['binary-f1-1'], 
           color='green', alpha=0.7, s=200, label='Imbalanced Data', edgecolors='black')
ax1.scatter(balanced_data_data['binary-f1-0'], balanced_data_data['binary-f1-1'], 
           color='purple', alpha=0.7, s=200, label='Balanced Data', edgecolors='black')

# Add diagonal line (no bias)   
min_val = min(df['binary-f1-0'].min(), df['binary-f1-1'].min())
max_val = max(df['binary-f1-0'].max(), df['binary-f1-1'].max())
ax1.plot([min_val, max_val], [min_val, max_val], 'k--', alpha=0.5, label='No Bias Line')

# Set axis limits to 0.2-1.0
ax1.set_xlim(0.3, 0.75)
ax1.set_ylim(0.3, 0.75)

ax1.set_xlabel('Binary F1 - Reject', fontsize=12, labelpad=15)
ax1.set_ylabel('Binary F1 - Accept', fontsize=12, labelpad=15)
ax1.set_title('Prompt Performance', fontsize=14, pad=20)
ax1.legend(fontsize=16)
ax1.grid(True, alpha=0.3)
ax1.tick_params(axis='both', which='major', labelsize=12)

plt.tight_layout()
plt.savefig('results/prompt_bias_analysis.png', dpi=300, bbox_inches='tight')
plt.show()

# Print summary statistics
print("=== PROMPT BIAS ANALYSIS ===")
print(f"\nAPO Method (first 10 prompts):")
print(f"Average bias: {apo_data['bias'].mean():.3f}")
print(f"Bias std: {apo_data['bias'].std():.3f}")
print(f"Positive bias prompts: {(apo_data['bias'] > 0).sum()}/10")
print(f"Negative bias prompts: {(apo_data['bias'] < 0).sum()}/10")

print(f"\nOPRO Method (next 10 prompts):")
print(f"Average bias: {opro_data['bias'].mean():.3f}")
print(f"Bias std: {opro_data['bias'].std():.3f}")
print(f"Positive bias prompts: {(opro_data['bias'] > 0).sum()}/10")
print(f"Negative bias prompts: {(opro_data['bias'] < 0).sum()}/10")

print(f"\nDetailed bias values:")
print("APO prompts bias:", [f"{bias:.3f}" for bias in apo_data['bias'].values])
print("OPRO prompts bias:", [f"{bias:.3f}" for bias in opro_data['bias'].values]) 