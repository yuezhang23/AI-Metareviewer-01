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
import asyncio
import aiohttp

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


# def _get_batch_results(batch_id, timeout=30):
#     max_poll_attempts = 100  # Maximum number of polling attempts
#     poll_interval = 5  # Seconds between polls
    
#     for attempt in range(max_poll_attempts):
#         try:
#             # Check batch status
#             batch_status = requests.get(
#                 f'https://api.openai.com/v1/batch/{batch_id}',
#                 headers={
#                     "Authorization": f"Bearer {config['OPENAI_API_KEY']}"
#                 },
#                 timeout=timeout
#             )
            
#             if batch_status.status_code == 200:
#                 status_data = batch_status.json()
#                 if status_data['status'] == 'completed':
#                     # Get the batch results
#                     batch_results = requests.get(
#                         f'https://api.openai.com/v1/batch/{batch_id}/results',
#                         headers={
#                             "Authorization": f"Bearer {config['OPENAI_API_KEY']}"
#                         },
#                         timeout=timeout
#                     )
                    
#                     if batch_results.status_code == 200:
#                         results = batch_results.json()
#                         return [result['response']['choices'][0]['message']['content'] 
#                                for result in results['responses']]
#                 elif status_data['status'] == 'failed':
#                     raise Exception(f"Batch processing failed: {status_data.get('error', 'Unknown error')}")
                
#             time.sleep(poll_interval)
            
#         except requests.exceptions.RequestException as e:
#             if attempt == max_poll_attempts - 1:
#                 raise Exception(f"Failed to get batch results after {max_poll_attempts} attempts: {str(e)}")
#             time.sleep(poll_interval)
    
#     raise Exception(f"Batch processing timed out after {max_poll_attempts * poll_interval} seconds")


# def chatgpt_batch(prompts, temperature, n=1, top_p=1, stop=None, max_tokens=1024, 
#                   presence_penalty=0, frequency_penalty=0, logit_bias={}, timeout=30):
#     # Create individual requests for each prompt
#     individual_requests = []
#     for prompt in prompts:
#         request = {
#             "method": "POST",
#             "url": "/v1/chat/completions",
#             "body": {
#                 "messages": [{"role": "user", "content": prompt}],
#                 "model": "gpt-4o-mini",
#                 "temperature": temperature,
#                 "n": n,
#                 "top_p": top_p,
#                 "stop": stop,
#                 "max_tokens": max_tokens,
#                 "presence_penalty": presence_penalty,
#                 "frequency_penalty": frequency_penalty,
#                 "logit_bias": logit_bias
#             }
#         }
#         individual_requests.append(request)

#     # Create the batch request
#     batch_request = {
#         "requests": individual_requests
#         }

#     # Submit the batch request
#     try:
#         response = requests.post(
#             'https://api.openai.com/v1/batch',
#             headers={
#                 "Authorization": f"Bearer {config['OPENAI_API_KEY']}",
#                 "Content-Type": "application/json"
#             },
#             json=batch_request,
#             timeout=timeout
#         )
        
#         if response.status_code == 200:
#             batch_id = response.json()['id']
#             return _get_batch_results(batch_id, timeout)
#         else:
#             raise Exception(f"Failed to create batch: {response.text}")        
#     except requests.exceptions.RequestException as e:
#         raise Exception(f"Request failed: {str(e)}")


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




async def _fetch_single_completion(session, prompt, temperature, n, top_p, stop, max_tokens, presence_penalty, frequency_penalty, logit_bias, max_retries=5):
    url = "https://api.openai.com/v1/chat/completions"
    headers = {
        "Authorization": f"Bearer {config['OPENAI_API_KEY']}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": "gpt-4o-mini",
        "messages": [{"role": "user", "content": prompt}],
        "temperature": temperature,
        "n": n,
        "top_p": top_p,
        "stop": stop,
        "max_tokens": max_tokens,
        "presence_penalty": presence_penalty,
        "frequency_penalty": frequency_penalty,
        "logit_bias": logit_bias
    }
    
    for attempt in range(max_retries):
        try:
            async with session.post(url, headers=headers, json=payload) as resp:
                if resp.status == 200:
                    response_json = await resp.json()
                    return response_json["choices"][0]["message"]["content"]
                elif resp.status == 429:  # Rate limit
                    retry_after = int(resp.headers.get('Retry-After', 5))
                    await asyncio.sleep(retry_after)
                    continue
                else:
                    error_text = await resp.text()
                    print(f"API Error - Status: {resp.status}, Error: {error_text}")
                    if attempt < max_retries - 1:
                        await asyncio.sleep(2 ** attempt)  # Exponential backoff
                        continue
                    raise Exception(f"Failed single completion: {resp.status}, {error_text}")
        except asyncio.TimeoutError:
            if attempt < max_retries - 1:
                await asyncio.sleep(2 ** attempt)
                continue
            raise
        except Exception as e:
            if attempt < max_retries - 1:
                await asyncio.sleep(2 ** attempt)
                continue
            raise

async def _run_batch(prompts, temperature, n, top_p, stop, max_tokens, presence_penalty, frequency_penalty, logit_bias, timeout=60):
    connector = aiohttp.TCPConnector(limit=20)  
    timeout = aiohttp.ClientTimeout(total=timeout)
    async with aiohttp.ClientSession(connector=connector, timeout=timeout) as session:
        tasks = [
            _fetch_single_completion(session, prompt, temperature, n, top_p, stop, max_tokens, presence_penalty, frequency_penalty, logit_bias)
            for prompt in prompts
        ]
        results = await asyncio.gather(*tasks, return_exceptions=True)
        return results

def chatgpt_batch(prompts, temperature, n=1, top_p=1, stop=None, max_tokens=1024, 
                  presence_penalty=0, frequency_penalty=0, logit_bias={}, timeout=60):
    try:
        results = asyncio.run(_run_batch(prompts, temperature, n, top_p, stop, max_tokens, presence_penalty, frequency_penalty, logit_bias, timeout))
        # Filter out any exceptions and return only successful results
        return [r for r in results if not isinstance(r, Exception)]
    except Exception as e:
        print(f"Batch processing failed: {str(e)}")
        return []

