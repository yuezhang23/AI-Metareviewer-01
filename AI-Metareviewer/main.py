import psycopg
from psycopg.rows import dict_row
from prompt_gpt import prompt
from prompt_deepseek import academic_review_decision
from utility import toString, getFScore, getBenchmarkDecision
from dotenv import dotenv_values
import os
from tiktoken import encoding_for_model

config = dotenv_values(".env")

os.makedirs("./data", exist_ok=True)
os.makedirs("./data/large_inputs", exist_ok=True)

tokenizer = encoding_for_model("gpt-4o-mini")  

def save_large_input(input_text, paper_id, token_count):
    filename = f"./data/large_inputs/paper_{paper_id}_tokens_{token_count}.txt"
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(input_text)
    return filename

QUERY = """
(SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%reject%' LIMIT 100) 
UNION ALL (SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%accept%' LIMIT 100);
"""
TOTAL = 200

totalAccept = 0
acceptMatch = 0
totalReject = 0
rejectMatch = 0
benchmarkAcceptMatch = 0
benchmarkRejectMatch = 0
count = 0

with open("./data/selected_ids.txt", "w") as s:
    pass

with open("./data/failures.txt", "w") as s:
    pass

with psycopg.connect(config["DB_CONFIG"], row_factory=dict_row) as conn:
    with conn.cursor() as cur:
        cur.execute(QUERY) 
        allMetareviews = cur.fetchall()
        for metareview in allMetareviews:
            count += 1
            print(str(count) + " / " + str(TOTAL))

            id = metareview["id"]

            decision = metareview["decision"]
            promptText = ""
            with open("./data/selected_ids.txt", "a") as s:
                s.write(id + ":" + decision + "\n")

            cur.execute("SELECT * FROM reviews WHERE id = %s", [id])
            allReviews = cur.fetchall()

            ratingsArray = []
            for review in allReviews: 
                promptText += toString(review)
                ratingsArray.append(review["rating"])

            try:
                token_count = len(tokenizer.encode(promptText))
                predictedResult = prompt(promptText).strip()
                # predictedResult = academic_review_decision(promptText).strip()
            except Exception as e:
                if "maximum context length" in str(e):
                    print(f"Error: Input exceeds maximum token length for paper {id}. Saving input...")
                    saved_file = save_large_input(promptText, id, token_count)
                    print(f"Large input saved to: {saved_file}")
                    predictedResult = "ERROR"  
                else:
                    print(f"Error occurred for paper {id}: {str(e)}")
                    predictedResult = "ERROR"

            benchmarkResult = getBenchmarkDecision(ratingsArray)
            if ("accept" in decision.lower()):
                totalAccept += 1
                if (predictedResult == "ACCEPT"):
                    acceptMatch += 1
                else:
                    with open("./data/failures.txt", "a") as f:
                        f.write(id + "\n")
                if (benchmarkResult == "ACCEPT"):
                    benchmarkAcceptMatch += 1
            else:
                totalReject += 1
                if (predictedResult == "REJECT"):
                    rejectMatch += 1
                else:
                    with open("./data/failures.txt", "a") as f:
                        f.write(id + "\n")
                if (benchmarkResult == "REJECT"):
                    benchmarkRejectMatch += 1

with open("./data/hypothesis.txt", "a") as h:  
    h.write("\n")
    print("Total Predicted Accepted: " + str(acceptMatch))
    h.write("Total Predicted Accepted: " + str(acceptMatch) + "\n")      

    print("Total Predicted Rejected: " + str(rejectMatch))
    h.write("Total Predicted Rejected: " + str(rejectMatch) + "\n")      

    print("Total Actual Accepted: " + str(totalAccept))
    h.write("Total Actual Accepted: " + str(totalAccept) + "\n")      

    print("Total Actual Rejected: " + str(totalReject))
    h.write("Total Actual Rejected: " + str(totalReject) + "\n")      

    if (totalAccept > 0):
        print("Success Rate on Accept: " + str(acceptMatch / totalAccept))
        h.write("Success Rate on Accept: " + str(acceptMatch / totalAccept)+ "\n")     

        print("Benchmark Success Rate on Accept: " + str(benchmarkAcceptMatch / totalAccept))
        h.write("Benchmark Success Rate on Accept: " + str(benchmarkAcceptMatch / totalAccept)+ "\n")      
    if (totalReject > 0):
        print("Success Rate on Reject: " + str(rejectMatch / totalReject))
        h.write("Success Rate on Reject: " + str(rejectMatch / totalReject)+ "\n")     

        print("Benchmark Success Rate on Reject: " + str(benchmarkRejectMatch / totalReject))
        h.write("Benchmark Success Rate on Reject: " + str(benchmarkRejectMatch / totalReject)+ "\n")      

    print("predicted F1 Score: " + str(getFScore(acceptMatch, totalReject - rejectMatch, totalAccept - acceptMatch)))
    h.write("predicted F1 Score: " + str(getFScore(acceptMatch, totalReject - rejectMatch, totalAccept - acceptMatch))+"\n")
    print("benchmark F1 Score: " + str(getFScore(benchmarkAcceptMatch, totalReject - benchmarkRejectMatch, totalAccept - benchmarkAcceptMatch)))
    h.write("benchmark F1 Score: " + str(getFScore(benchmarkAcceptMatch, totalReject - benchmarkRejectMatch, totalAccept - benchmarkAcceptMatch))+"\n")
