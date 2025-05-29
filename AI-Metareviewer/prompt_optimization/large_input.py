import pandas as pd
import tiktoken
import os

def count_tokens(text):
    encoding = tiktoken.encoding_for_model("gpt-4o-mini")
    return len(encoding.encode(text))

def analyze_large_inputs(csv_path, output_path, token_threshold=6000):
    # Read the CSV file
    df = pd.read_csv(csv_path, sep=';', names=['id', 'reviews', 'decision'])
    
    # Calculate token counts for each review
    df['token_count'] = df['reviews'].apply(count_tokens)
    
    # Calculate statistics
    avg_tokens = df['token_count'].mean()
    max_tokens = df['token_count'].max()
    min_tokens = df['token_count'].min()
    total_reviews = len(df)
    
    # Filter reviews that exceed the token threshold
    large_reviews = df[df['token_count'] > token_threshold]
    
    # Create output directory if it doesn't exist
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    # Write results to output file
    with open(output_path, 'w') as f:
        f.write(f"Token Statistics:\n")
        f.write(f"Total Reviews: {total_reviews}\n")
        f.write(f"Average Token Length: {avg_tokens:.2f}\n")
        f.write(f"Minimum Token Length: {min_tokens}\n")
        f.write(f"Maximum Token Length: {max_tokens}\n")
        f.write(f"Number of Reviews > {token_threshold} tokens: {len(large_reviews)}\n")
        f.write("\n" + "="*50 + "\n\n")
        
        f.write(f"Reviews exceeding {token_threshold} tokens:\n\n")
        for _, row in large_reviews.iterrows():
            f.write(f"ID: {row['id']}\n")
            f.write(f"Token Count: {row['token_count']}\n")
            f.write("-" * 50 + "\n")

if __name__ == "__main__":
    # Define paths
    csv_path = "data/metareviewer_data_test_200.csv"
    output_path = f"results/large_inputs_6000.txt"
    
    # Run analysis
    analyze_large_inputs(csv_path, output_path)
