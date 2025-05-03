from dotenv import dotenv_values
from openai import OpenAI
import tiktoken
from transformers import AutoTokenizer  # Install with pip install transformers

config = dotenv_values(".env")

# add Analyze the provided reviews in every aspect, pariticularly in ratings, confidence and presentation. Do not lightly hand out conclusion.
prompt = "` along with `"



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


def make_decision(input_text, context=""):
    compressed_text = summarize_text(input_text)
    
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=[
            {"role": "system", "content": prompt},
            {"role": "user", "content": compressed_text}  
        ],
        temperature=0.0,
        max_tokens=2,  
    )
    
    decision = response.choices[0].message.content.strip().upper()
    return decision 

