# Prompt Bias Analysis

## Overview
This analysis compares the bias between two different prompt generation methods using binary F1 scores. The bias is measured as the difference between F1 scores for class 0 and class 1.

## Data Format
- Each line contains `binary-f1-0` and `binary-f1-1` values for a specific prompt
- First 10 prompts (1-10) come from Method 1
- Last 10 prompts (11-20) come from Method 2

## Bias Definition
**Bias = binary-f1-0 - binary-f1-1**

- **Positive bias**: Model performs better on class 0 than class 1
- **Negative bias**: Model performs better on class 1 than class 0
- **Zero bias**: Model performs equally well on both classes

## Key Findings

### Method 1 (Prompts 1-10)
- **Mean bias**: 0.113 (positive bias toward class 0)
- **Standard deviation**: 0.115
- **Mean absolute bias**: 0.129

### Method 2 (Prompts 11-20)
- **Mean bias**: -0.006 (nearly balanced)
- **Standard deviation**: 0.093
- **Mean absolute bias**: 0.066

### Statistical Comparison
- **t-statistic**: 2.536
- **p-value**: 0.021
- **Significance**: Yes (α=0.05)

## Interpretation

1. **Method 1 shows significant positive bias** toward class 0, meaning prompts from this method consistently perform better on class 0 than class 1.

2. **Method 2 shows nearly balanced performance** with minimal bias between classes.

3. **Method 2 has lower bias variability** (std: 0.093 vs 0.115), indicating more consistent performance across classes.

4. **The difference is statistically significant**, suggesting that the two methods produce prompts with fundamentally different bias characteristics.

## Visualization
The analysis generates a comprehensive plot with four panels:
1. **Scatter plot**: F1-0 vs F1-1 scores by method
2. **Box plot**: Bias distribution by method
3. **Bar chart**: Individual prompt bias comparison
4. **Statistics table**: Summary statistics by method

## Files Generated
- `bias_comparison.png`: Comprehensive visualization
- `bias_analysis_results.csv`: Detailed results with bias calculations
- `analyze_bias.py`: Analysis script

## Recommendations
- **Method 2** appears to produce more balanced prompts with less bias
- Consider using **Method 2** for applications requiring fair performance across classes
- If class 0 performance is prioritized, **Method 1** might be preferred
- Further investigation into prompt characteristics that lead to reduced bias in Method 2 