from openai import OpenAI
from dotenv import dotenv_values
import os
from datetime import datetime


config = dotenv_values(".env")
client = OpenAI(api_key=config["OPENAI_API_KEY"])

# basic prompt 
# add1 "The vast majority of papers are accepted, only 0.05 of papers are rejected at the conference."
# add2 "Do not lightly hand out ACCEPT." 
# v add3 Analyze the provided reviews in every aspect to not lightly hand out conclusion. 
# x (exceed limit) add4 Analyze the provided reviews in terms of 'summary', 'soundness', 'presentation', 'contribution', 'strengths', 'weaknesses', 'limitations', 'rating', and 'confidence'. 
prompt_text = '''Analyze the reviews provided, decide if the paper in question would be accepted at an academic conference. 
The vast majority of papers are accepted. About 0.05 of papers are rejected at the conference.
Answer only ACCEPT or REJECT'''


def prompt(input, context=""):
    completion = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {
                "role": "system",
                "content": prompt_text + context
            },
            {
                "role": "user",
                "content": input
            }
        ],
        max_tokens=10
    )
    print(completion.choices[0].message.content)
    return completion.choices[0].message.content
