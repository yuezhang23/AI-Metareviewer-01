import pandas as pd
import psycopg
import csv
from psycopg.rows import dict_row
from dotenv import dotenv_values

config = dotenv_values(".env")

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



def get_train_examples(qry):
    exs = []
    with psycopg.connect(config["DB_CONFIG"], row_factory=dict_row) as conn:
        with conn.cursor() as cur:
            cur.execute(qry)
            allMetareviews = cur.fetchall()
            for metareview in allMetareviews:
                id = metareview["id"]
                decision = metareview["decision"]
                promptText = ""

                cur.execute("SELECT * FROM reviews WHERE id = %s", [id])
                allReviews = cur.fetchall()

                for review in allReviews: 
                    promptText += toString(review)

                exs.append({'id': id, 'text': promptText, 'label': 1 if "accept" in decision.lower() else 0})
    header = ['id', 'text', 'label']
    return header, exs

QUERY1 = """(SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%accept%' LIMIT 50) UNION ALL 
(SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%reject%' LIMIT 50);"""

QUERY2 = """(SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%accept%' OFFSET 50 LIMIT 25)
UNION ALL (SELECT id, decision FROM metareviews WHERE LOWER(decision) LIKE '%reject%' OFFSET 50 LIMIT 25);
"""


with open('./data/metareviewer_balanced_data.csv', 'w', newline='', encoding="utf-8") as file:
    header, exs = get_train_examples(QUERY1)
    writer = csv.DictWriter(file, fieldnames=header, delimiter=";")
    writer.writeheader()  # Write the header row
    writer.writerows(exs) # Write multiple data rows

with open('./data/metareviewer_test_data.csv', 'w', newline='', encoding="utf-8") as file:
    header, exs = get_train_examples(QUERY2)
    writer = csv.DictWriter(file, fieldnames=header, delimiter=";")
    writer.writeheader()  # Write the header row
    writer.writerows(exs) # Write multiple data rows


# def get_train_examples():
#     df = pd.read_csv('./metareviewer_data_test.csv', sep=';', header=None)
#     exs = df.reset_index().to_dict('records')
#     exs = [{'id': x[0], 'text': x[1], 'label': int(x[2])} for x in exs]
#     print(exs)
#     return exs

# get_train_examples()