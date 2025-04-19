from dotenv import dotenv_values
from openai import OpenAI
import tiktoken
from transformers import AutoTokenizer  # Install with pip install transformers

config = dotenv_values(".env")

# add Analyze the provided reviews in every aspect, pariticularly in ratings, confidence and presentation. Do not lightly hand out conclusion.
prompt = '''Based on the reviews provided, decide if the paper in question would be accepted for presentation at an academic conference.
Analyze the provided reviews in every aspect, pariticularly in ratings, confidence and presentation. Do not lightly hand out conclusion.
Answer only ACCEPT or REJECT.
'''



client = OpenAI(api_key=config["DEEPSEEK_API_KEY"], base_url="https://api.deepseek.com")


def academic_review_decision(input_text, context=""):

    tokenizer = AutoTokenizer.from_pretrained("deepseek-ai/deepseek-llm-67b-chat")
    truncated_text = tokenizer.decode(
        tokenizer.encode(input_text, truncation=True, max_length=60000)  # Leave room for system prompt
    )
    
    
    response = client.chat.completions.create(
        model="deepseek-chat",
        messages=[
            {"role": "system", "content": prompt},
            {"role": "user", "content": truncated_text}  # Use truncated input
        ],
        temperature=0.0,
        max_tokens=2,  # Only need 1 word response
    )
    
    decision = response.choices[0].message.content.strip().upper()
    return decision 

