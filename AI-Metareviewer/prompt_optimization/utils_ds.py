import os
os.environ["TOKENIZERS_PARALLELISM"] = "false"

from dotenv import dotenv_values
from openai import OpenAI
import tiktoken
from transformers import AutoTokenizer  # Install with pip install transformers
import utils
config = dotenv_values(".env")
import utils_ds
import time

# Set maximum token length for the model
MAX_TOKENS = 6000

client = OpenAI(api_key=config["DEEPSEEK_API_KEY"], base_url="https://api.deepseek.com")

tokenizer = AutoTokenizer.from_pretrained("deepseek-ai/deepseek-r1")

def summarize_text(text, compression_ratio=0.8):
    """
    Compress long text using DeepSeek's summarization capability
    compression_ratio: Target size relative to original (e.g., 0.2 = 20%)
    """
    # Check token length and chunk if necessary
    tokens = tokenizer.encode(text)
    if len(tokens) > MAX_TOKENS:
        text = summarize_very_long_text(text)
    
    prompt = f"""Compress the following text while preserving key information.
    Target length: {int(len(text)*compression_ratio)} characters.
    Text: {text}"""
    
    response = client.chat.completions.create(
        model="deepseek-r1",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.0  # Lower temperature for more factual compression
    )
    
    return response.choices[0].message.content

def chunk_text(text, chunk_size=3000):
    """
    Split text into chunks that fit within model's token limit
    """
    tokens = tokenizer.encode(text)
    chunks = []
    current_chunk = []
    current_length = 0
    
    for token in tokens:
        if current_length + 1 > MAX_TOKENS:
            chunks.append(tokenizer.decode(current_chunk))
            current_chunk = [token]
            current_length = 1
        else:
            current_chunk.append(token)
            current_length += 1
    
    if current_chunk:
        chunks.append(tokenizer.decode(current_chunk))
    
    return chunks

def summarize_very_long_text(text):
    chunks = chunk_text(text)
    summaries = []
    for chunk in chunks:
        summaries.append(summarize_text(chunk))
    return "\n".join(summaries)

def make_response(gradient_prompt, n=1, temperature=0.7, model="deepseek-chat"):
    # Get DeepSeek response
    messages = [
        {"role": "system", "content": ""},
        {"role": "user", "content": gradient_prompt},
    ]
    
    response = client.chat.completions.create(
        model=model,
        messages=messages,
        temperature=temperature,
        max_tokens=3000,
    )  
    # Extract completions
    return response.choices[0].message.content.strip()


def make_decision(input_text="", prompt="", temperature=0.0, model="deepseek-chat"):
    max_retries = 5
    retry_count = 0
    
    while retry_count < max_retries:
        try:
            response = client.chat.completions.create(
                model=model,
                messages=[
                    {"role": "system", "content": prompt},
                    {"role": "user", "content": input_text},       
                ],
                temperature=temperature,
                max_tokens=2,  # Only need 1 word response
            )            
            decision = response.choices[0].message.content.strip().upper()
            return decision
            
        except Exception as e:
            retry_count += 1
            error_message = f"Error occurred (attempt {retry_count}/{max_retries}): {str(e)}"
            print(error_message)
            
            if retry_count == max_retries:
                print("Max retries reached. Unable to make decision.")
                return None
                
            # Wait for a short time before retrying (optional)
            time.sleep(1)

