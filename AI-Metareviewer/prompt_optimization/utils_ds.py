from dotenv import dotenv_values
from openai import OpenAI
import tiktoken
from transformers import AutoTokenizer  # Install with pip install transformers
import utils
config = dotenv_values(".env")
import utils_ds
import time

# add Analyze the provided reviews in every aspect, pariticularly in ratings, confidence and presentation. Do not lightly hand out conclusion.
prompt = '''Based on the reviews provided, decide if the paper in question would be accepted for presentation at an academic conference.
Analyze the provided reviews in every aspect, pariticularly in ratings, confidence and presentation. Do not lightly hand out conclusion.
Answer only ACCEPT or REJECT.
'''



client = OpenAI(api_key=config["DEEPSEEK_API_KEY"], base_url="https://api.deepseek.com")

def summarize_text(text, compression_ratio=0.8):
    """
    Compress long text using DeepSeek's summarization capability
    compression_ratio: Target size relative to original (e.g., 0.2 = 20%)
    """
    prompt = f"""Compress the following text while preserving key information.
    Target length: {int(len(text)*compression_ratio)} characters.
    Text: {text}"""
    
    response = client.chat.completions.create(
        model="deepseek-r1",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3  # Lower temperature for more factual compression
    )
    
    return response.choices[0].message.content

def chunk_text(text, chunk_size=3000):
    return [text[i:i+chunk_size] for i in range(0, len(text), chunk_size)]

def summarize_very_long_text(text):
    chunks = chunk_text(text)
    summaries = []
    for chunk in chunks:
        summaries.append(summarize_text(chunk))
    return "\n".join(summaries)


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
                    {"role": "Assistant", "content": ""}  # Model generates here               
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

