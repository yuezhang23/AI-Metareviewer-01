"""
https://oai.azure.com/portal/be5567c3dd4d49eb93f58914cccf3f02/deployment
clausa gpt4
"""

import time
import requests
from dotenv import dotenv_values
import string
import requests
import uuid
import json

config = dotenv_values(".env")

def parse_sectioned_prompt(s):

    result = {}
    current_header = None

    for line in s.split('\n'):
        line = line.strip()

        if line.startswith('# '):
            # first word without punctuation
            current_header = line[2:].strip().lower().split()[0]
            current_header = current_header.translate(str.maketrans('', '', string.punctuation))
            result[current_header] = ''
        elif current_header is not None:
            result[current_header] += line + '\n'

    return result


def _prepare_request(messages, temperature, n, top_p, stop, max_tokens, presence_penalty, frequency_penalty, logit_bias):
    return {
        "messages": messages,
        "model": "gpt-4o-mini",
        "temperature": temperature,
        "n": n,
        "top_p": top_p,
        "stop": stop,
        "max_tokens": max_tokens,
        "presence_penalty": presence_penalty,
        "frequency_penalty": frequency_penalty,
        "logit_bias": logit_bias
    }


def _process_batch_response(batch_response):
    responses = []
    for response in batch_response.get('responses', []):
        if response.get('status_code') == 200:
            response_data = response.get('body', {})
            if 'choices' in response_data:
                responses.extend([choice['message']['content'] for choice in response_data['choices']])
    return responses


def chatgpt_batch(prompts, temperature, n=1, top_p=1, stop=None, max_tokens=1024, 
                  presence_penalty=0, frequency_penalty=0, logit_bias={}, timeout=30):

    messages_list = [{"role": "user", "content": p} for p in prompts]

    batch_requests = _prepare_request(messages_list, temperature, n, top_p, stop, max_tokens, 
                              presence_penalty, frequency_penalty, logit_bias)
 
    retries = 0
    while True:
        try:
            r = requests.post('https://api.openai.com/v1/chat/completions',
                headers = {
                    "Authorization": f"Bearer {config['OPENAI_API_KEY']}",
                    "Content-Type": "application/json"
                },
                json = batch_requests,
                timeout=timeout
            )
            
            if r.status_code == 200:
                return _process_batch_response(r.json())
            else:
                print(f"API Error - Status Code: {r.status_code}")
                print(f"Error Response: {r.text}")
                retries += 1
                time.sleep(1)
                
        except requests.exceptions.ReadTimeout:
            print("Request timed out")
            time.sleep(1)
            retries += 1



def chatgpt(prompt, temperature=0.7, n=1, top_p=1, stop=None, max_tokens=1024, 
                  presence_penalty=0, frequency_penalty=0, logit_bias={}, timeout=10):
    messages = [{"role": "user", "content": prompt}]
    payload = {
        "messages": messages,
        "model": "gpt-4o-mini",
        "temperature": temperature,
        "n": n,
        "top_p": top_p,
        "stop": stop,
        "max_tokens": max_tokens,
        "presence_penalty": presence_penalty,
        "frequency_penalty": frequency_penalty,
        "logit_bias": logit_bias
    }
    
    max_retries = 5
    base_delay = 1
    
    for retry in range(max_retries):
        try:
            r = requests.post('https://api.openai.com/v1/chat/completions',
                headers = {
                    "Authorization": f"Bearer {config['OPENAI_API_KEY']}",
                    "Content-Type": "application/json"
                },
                json = payload,
                timeout=timeout
            )
            
            if r.status_code == 200:
                r = r.json()
                return [choice['message']['content'] for choice in r['choices']]
            elif r.status_code == 429:  # Rate limit error
                retry_after = int(r.headers.get('Retry-After', base_delay * (2 ** retry)))
                print(f"Rate limit hit. Waiting {retry_after} seconds...")
                time.sleep(retry_after)
            else:
                print(f"API Error - Status Code: {r.status_code}")
                if retry < max_retries - 1:
                    wait_time = base_delay * (2 ** retry)
                    time.sleep(wait_time)
                
        except requests.exceptions.RequestException:
            if retry < max_retries - 1:
                wait_time = base_delay * (2 ** retry)
                time.sleep(wait_time)
    
    raise Exception(f"Failed to get response after {max_retries} retries")


def instructGPT_logprobs(prompt, temperature=0.7):
    payload = {
        "prompt": prompt,
        "model": "gpt-4o-mini",
        "temperature": temperature,
        "max_tokens": 1,
        "logprobs": 1,
        "echo": True
    }
    while True:
        try:
            r = requests.post('https://api.openai.com/v1/completions',
                headers = {
                    "Authorization": f"Bearer {config['OPENAI_API_KEY']}",
                    "Content-Type": "application/json"
                },
                json = payload,
                timeout=30
            )  
            if r.status_code != 200:
                time.sleep(2)
                retries += 1
            else:
                break
        except requests.exceptions.ReadTimeout:
            time.sleep(5)
    r = r.json()
    return r['choices']


