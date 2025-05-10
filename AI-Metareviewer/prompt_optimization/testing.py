import pandas as pd
import psycopg
import csv
from psycopg.rows import dict_row
import os
ratingScores = {
    1: "Very Strong Reject: For instance, a paper with incorrect statements, improper (e.g., offensive) language, unaddressed ethical considerations, incorrect results and/or flawed methodology (e.g., training using a test set).",
    2: "Strong Reject: For instance, a paper with major technical flaws, and/or poor evaluation, limited impact, poor reproducibility and mostly unaddressed ethical considerations.",
    3: "reject, not good enough",
    4: "Borderline reject: Technically solid paper where reasons to reject, e.g., limited evaluation, outweigh reasons to accept, e.g., good evaluation. Please use sparingly.",
    5: "marginally below the acceptance threshold",
    6: "marginally above the acceptance threshold",
    7: "Accept: Technically solid paper, with high impact on at least one sub-area, or moderate-to-high impact on more than one areas, with good-to-excellent evaluation, resources, reproducibility, and no unaddressed ethical considerations.",
    8: "accept, good paper",
    9: "Very Strong Accept: Technically flawless paper with groundbreaking impact on at least one area of AI/ML and excellent impact on multiple areas of AI/ML, with flawless evaluation, resources, and reproducibility, and no unaddressed ethical considerations.",
    10: "strong accept, should be highlighted at the conference"
}

confidenceScores = {
    1: "Your assessment is an educated guess. The submission is not in your area or the submission was difficult to understand. Math/other details were not carefully checked.",
    2: "You are willing to defend your assessment, but it is quite likely that you did not understand the central parts of the submission or that you are unfamiliar with some pieces of related work. Math/other details were not carefully checked.",
    3: "You are fairly confident in your assessment. It is possible that you did not understand some parts of the submission or that you are unfamiliar with some pieces of related work. Math/other details were not carefully checked.",
    4: "You are confident in your assessment, but not absolutely certain. It is unlikely, but not impossible, that you did not understand some parts of the submission or that you are unfamiliar with some pieces of related work.",
    5: "You are absolutely certain about your assessment. You are very familiar with the related work and checked the math/other details carefully."
}

miscScores = {
    1: "poor",
    2: "fair",
    3: "good",
    4: "excellent"
}

fields = ['summary', 'soundness', 'presentation', 'contribution', 'strengths', 'weaknesses', 'limitations', 'rating', 'confidence']

def toString(review):
    ret = "REVIEW \n"
    for field in fields:
        ret += field.capitalize() + ":" + "\n"
        ret += str(review[field])
        if isinstance(review[field], int):
            if (field == "rating"):
                ret += ": " + ratingScores[review[field]]
            elif (field == "confidence"):
                ret += ": " + confidenceScores[review[field]]
            else:
                ret += ": " + miscScores[review[field]]
        ret += "\n\n"

    return ret


def get_train_examples():
    # Get test data (100 accept + 100 reject)
    test_exs = []
    with psycopg.connect(os.getenv("DB_CONFIG"), row_factory=dict_row) as conn:
        with conn.cursor() as cur:
            # Get test data
            cur.execute("""(SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%reject%' ORDER BY RANDOM() LIMIT 100) 
                        UNION ALL 
                        (SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%accept%' ORDER BY RANDOM() LIMIT 100)""")
            test_metareviews = cur.fetchall()
            
            for metareview in test_metareviews:
                id = metareview["id"]
                decision = metareview["decision"]
                promptText = ""

                cur.execute("SELECT * FROM reviews WHERE id = %s", [id])
                allReviews = cur.fetchall()

                for review in allReviews: 
                    promptText += toString(review)

                test_exs.append({'id': id, 'text': promptText, 'label': 1 if "accept" in decision.lower() else 0})

    # Get training data (800 accept + 800 reject)
    train_exs = []
    with psycopg.connect(os.getenv("DB_CONFIG"), row_factory=dict_row) as conn:
        with conn.cursor() as cur:
            # Get training data (excluding test IDs)
            test_ids = [ex['id'] for ex in test_exs]
            test_ids_str = ','.join([f"'{id}'" for id in test_ids])
            
            cur.execute(f"""((SELECT id, decision FROM metareviews 
                            WHERE LOWER(decision) LIKE '%reject%' 
                            AND id NOT IN ({test_ids_str})
                            ORDER BY RANDOM() LIMIT 800)
                        UNION ALL 
                        (SELECT id, decision FROM metareviews 
                            WHERE LOWER(decision) LIKE '%accept%' 
                            AND id NOT IN ({test_ids_str})
                            ORDER BY RANDOM() LIMIT 800))""")
            train_metareviews = cur.fetchall()
            
            for metareview in train_metareviews:
                id = metareview["id"]
                decision = metareview["decision"]
                promptText = ""

                cur.execute("SELECT * FROM reviews WHERE id = %s", [id])
                allReviews = cur.fetchall()

                for review in allReviews: 
                    promptText += toString(review)

                train_exs.append({'id': id, 'text': promptText, 'label': 1 if "accept" in decision.lower() else 0})

    # Save test data
    header = ['id', 'text', 'label']
    with open('./data/metareviewer_data_test_200.csv', 'w', newline='', encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=header, delimiter=";")
        writer.writeheader()  
        writer.writerows(test_exs)

    # Save training data
    with open('./data/metareviewer_data_train_800.csv', 'w', newline='', encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=header, delimiter=";")
        writer.writeheader()  
        writer.writerows(train_exs)

    return train_exs, test_exs

def get_additional_train_examples():
    # Read existing data from data.csv
    existing_ids = set()
    try:
        with open('./data/metareviewer_data_train_800.csv', 'r', newline='', encoding="utf-8") as file:
            reader = csv.DictReader(file, delimiter=";")
            for row in reader:
                existing_ids.add(row['id'])
    except FileNotFoundError:
        print("data.csv not found. Starting with empty set of existing IDs.")

    # Get additional training data (800 examples)
    additional_exs = []
    with psycopg.connect(os.getenv("DB_CONFIG"), row_factory=dict_row) as conn:
        with conn.cursor() as cur:
            # Get additional training data (excluding existing IDs)
            existing_ids_str = ','.join([f"'{id}'" for id in existing_ids]) if existing_ids else "''"
            
            cur.execute(f"""((SELECT id, decision FROM metareviews 
                            WHERE LOWER(decision) LIKE '%reject%' 
                            AND id NOT IN ({existing_ids_str})
                            ORDER BY RANDOM() LIMIT 800)
                        UNION ALL 
                        (SELECT id, decision FROM metareviews 
                            WHERE LOWER(decision) LIKE '%accept%' 
                            AND id NOT IN ({existing_ids_str})
                            ORDER BY RANDOM() LIMIT 800))""")
            additional_metareviews = cur.fetchall()
            
            for metareview in additional_metareviews:
                id = metareview["id"]
                decision = metareview["decision"]
                promptText = ""

                cur.execute("SELECT * FROM reviews WHERE id = %s", [id])
                allReviews = cur.fetchall()

                for review in allReviews: 
                    promptText += toString(review)

                additional_exs.append({'id': id, 'text': promptText, 'label': 1 if "accept" in decision.lower() else 0})

    # Save additional data
    header = ['id', 'text', 'label']
    with open('./data/additional_data_800+800.csv', 'w', newline='', encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=header, delimiter=";")
        writer.writeheader()  
        writer.writerows(additional_exs)

    return additional_exs

# def get_train_examples():
#     df = pd.read_csv('./metareviewer_data_test.csv', sep=';', header=None)
#     exs = df.reset_index().to_dict('records')
#     exs = [{'id': x[0], 'text': x[1], 'label': int(x[2])} for x in exs]
#     print(exs)
#     return exs

# get_train_examples()
get_additional_train_examples()