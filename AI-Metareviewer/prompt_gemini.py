from google import genai
from dotenv import dotenv_values

config = dotenv_values(".env")

client = genai.Client(api_key=config["GEMINI_API_KEY"])


def prompt(input):
    response = client.models.generate_content(model='gemini-2.0-flash-exp', contents=
                        '''Academic papers submitted to a conference are often reviewed by independent reviewers, who will give a summary of the paper, 
                        their thoughts, any weaknesses, a score of the paper, and their confidence in their score. A "meta-reviewer" then aggregates these 
                        reviews and decides whether to accept or deny the paper. 

                        You are to act as a meta-reviewer. I will provide some reviews of a paper, and you must decide based on the content of the review whether 
                        to accept or deny the paper. The input will be a series of key-value pairs. Separate reviews will be separated by 4 newlines. Answer only ACCEPT or REJECT
                        ''' + input)
    return response.text
