# Utilizing LLMs as Academic Chairs for Top ML Conferences

## 1. Introduction

### 1.1 Task Description
The primary objective is to utilize Large Language Models (LLMs) to function as Academic Chairs for top Machine Learning conferences, specifically focusing on paper acceptance decisions.

### 1.2 Background
The study focuses on nonparametric algorithms with broad applicability. The ProTeGi algorithm was tested with various hyperparameter combinations to achieve higher generalization performance. Previous work includes human prompting results, which will be discussed below.

## 2. Previous Work

### 2.1 Biased Prompt Results
- **Prompt**: "Analyze the reviews provided, decide if the paper in question would be accepted at an academic conference. The vast majority of papers are accepted. About 0.05 of papers are rejected at the conference. Answer only ACCEPT or REJECT"
- **Test F1 Score**: 0.75

### 2.2 Neutral Prompts (Initial APO Sections)
1. **Option 1**: "Given the following reviews (text), determine if a paper would be accepted (Yes) or not (No) by an academic conference."
   - Test F1 Score: 0.5

2. **Option 2**: "Given the following reviews, determine if the paper being reviewed would be accepted at an academic conference."
   - Test F1 Score: 0.5

## 3. Experimental Setup

### 3.1 Dataset Configuration
- **Source**: venue_id = 'NeurIPS.cc/2024/Conference'
- **Training Dataset**: 800 accept + 800 reject
- **Test Dataset**: 800 accept + 800 reject
- **Test Data**: 100 accept + 100 reject

### 3.2 Model Configuration
- **Models**: ["gpt-4o-mini", "gpt-4.1-nano"]
- **Evaluation Models**: ["gpt-4o-mini", "gpt-4.1-nano"]
- **Beam Size**: 5
- **Number of Test Examples**: 200

### 3.3 Hyperparameter Combinations
#### 3.3.1 Expansion Parameters
[n_gradients, errors_per_gradient, gradients_per_error, steps_per_gradient, mc_samples_per_step, max_expansion_factor]

| Combo ID | Parameters | Description |
|----------|------------|-------------|
| Combo 0 | [4, 4, 1, 1, 2, 8] | From paper |
| Combo 1 | [4, 4, 3, 2, 0, 6] | '44320-6' |
| Combo 2 | [4, 4, 3, 1, 0, 6] | |
| Combo 3 | [4, 4, 3, 1, 1, 6] | |
| Combo 4 | [4, 4, 3, 1, 2, 6] | |
| Combo 5 | [6, 4, 3, 2, 0, 6] | |
| Combo 6 | [6, 4, 3, 2, 0, 8] | |
| Combo 7 | [6, 4, 3, 2, 0, 8] | |
| Combo 8 | [6, 6, 1, 1, 2, 6] | |

#### 3.3.2 Evaluation Parameters
[eval_rounds, eval_prompts_per_round, samples_per_eval]

| Combo ID | Parameters | Eval Budget |
|----------|------------|-------------|
| Combo 0 | [8, 8, 32] | 2048 (from paper) |
| Combo 1 | [5, 3, 4] | 60 |
| Combo 2 | [5, 3, 8] | 120 |
| Combo 3 | [5, 6, 8] | 240 |
| Combo 4 | [5, 8, 16] | 640 |
| Combo 5 | [10, 5, 16] | 800 |

## 4. Results and Analysis

### 4.1 Evaluation Budget Impact
The evaluation budget is calculated as:
```
config['eval_budget'] = config['samples_per_eval'] * config['eval_rounds'] * config['eval_prompts_per_round']
```

#### 4.1.1 Experiment 01: BF Evaluator with GPT 4.1-nano

| Eval Budget | 150 | 240 | 360 | 600 | 800 |
|-------------|-----|-----|-----|-----|-----|
| Prompts for Eval | 30 | 30 | 30 | 40 | 40 |
| Test F1 Score | 0.665 | 0.69 | 0.655 | 0.665 | 0.67 |

#### 4.1.2 Experiment 02: UCB Evaluator with GPT 4o-mini

| Eval Budget | 60 | 120 | 240 | 640 |
|-------------|-----|-----|-----|-----|
| Prompts for Eval | 30 | 30 | 30 | 60 |
| Test F1 Score | 0.74 | 0.745 | 0.75/0.735 | 0.73 |
| Peak Round | 5 | 5 | 4 | 3 |

### 4.2 Prompt Expansion Analysis

#### 4.2.1 Experiment 03: BF Evaluator with GPT4o-mini (Eval Budget: 240)

| Expansion Combo | 44112-8 | 44310-6 | 44311-6 | 44320-6 | 64320-6 | 66112-6 |
|----------------|---------|---------|---------|---------|---------|---------|
| Test F1 Score | 0.715 | 0.72 | 0.73 | 0.76/0.74 | 0.75 | 0.725 |
| Peak Round | 4 | 3 | 4 | 6 | 4 | 4 |
| Total API calls | 14 | 17 | 30 | 17 | 25 | 20 |
| New Prompts | 14 | 12 | 25 | 24 | 36 | 20 |

#### 4.2.2 Experiment 04: UCB Evaluator with GPT4o-mini (Eval Budget: 240)

| Expansion Combo | 44312-6 | 44610-6 | 48520-6 | 44320-6 | 64320-8 | 66112-6 |
|----------------|---------|---------|---------|---------|---------|---------|
| Test F1 Score | 0.73 | 0.745 | 0.725 | 0.75/0.735 | 0.725 | 0.735 |
| Peak Round | 3 | 5 | 5 | 4 | 4 | 4 |
| Total API calls | 30 | 29 | 25 | 17 | 25 | 20 |
| New Prompts | 38 | 24 | 40 | 24 | 36 | 20 |

### 4.3 Model Comparison
- **Hybrid Model**: Uses GPT-4o-mini for reasoning (gradients and prompt synonyms) and GPT 4.1-nano for scoring and evaluation
- GPT-4o-mini shows approximately 7% better test performance compared to GPT-4.1-nano
- Hybrid model performance varies based on expansion hyperparameters

### 4.4 UCB Bandits Exploration
#### 4.4.1 Experiment 05: UCB Parameter Comparison (Eval Budget: 60)

| Parameter | c2.0 | c1.0 |
|-----------|------|------|
| Test F1 Score | 0.74 | 0.725 |
| Peak Round | 5 | 4 |

## 5. Discussion

### 5.1 Gradient Prompt Optimization
The gradient prompt optimization shows potential for better performance but exhibits more instability. For example, with `num_feedbacks = 1`:
- **Original Prompt** (from paper) maintains exact feedback count
- **Modified Prompt 01** may return more than one feedback during execution

#### 5.1.1 Prompt Comparison

**Original Prompt**:
```
I'm trying to write a zero-shot classifier prompt.

My current prompt is:
"{prompt}"

But this prompt gets the following examples wrong:
{error_string}

give {num_feedbacks} reasons why the prompt could have gotten these examples wrong.
Wrap each reason with <START> and <END>
```

**Modified Prompt 01**:
```
I'm trying to write a zero-shot classifier prompt.

My current prompt is:
"{prompt}"

But this prompt gets the following examples wrong:
{error_string}

give {num_feedbacks} different reasons why the prompt incorrectly classified these examples.
Wrap each reason with <START> and <END>
```

#### 5.1.2 Performance Comparison

| Prompt Type | 44112-8 (Paper) | 44320-6 |
|-------------|-----------------|---------|
| Original | 0.71 / R6 | 0.685 / R5 |
| Modified 01 | 0.715 / R4 | 0.75 / R6 |

    

