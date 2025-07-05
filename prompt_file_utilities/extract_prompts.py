def extract_prompts(file_path):
    prompt_candidates = []
    
    with open(file_path, 'r', encoding='utf-8') as file:
        for line in file:
            if line.strip().startswith('# Task'):
                # Remove the '# Task\n' prefix and any surrounding quotes
                prompt = line.strip()
                if prompt.startswith('"') and prompt.endswith('"'):
                    prompt = prompt[1:-1]
                prompt_candidates.append(prompt)
    
    return prompt_candidates

if __name__ == "__main__":
    file_path = "AI-Metareviewer/prompt_optimization/results/01bf/eval-240/top_prompts.out"
    prompts = extract_prompts(file_path)
    print(f"Found {len(prompts)} prompts:")
    for i, prompt in enumerate(prompts, 1):
        print(f"\nPrompt {i}:")
        print(prompt) 