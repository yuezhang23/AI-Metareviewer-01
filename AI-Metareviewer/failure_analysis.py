import psycopg
from psycopg.rows import dict_row
import statistics
from dotenv import dotenv_values
import matplotlib.pyplot as plt
import numpy as np
from sklearn.metrics import confusion_matrix
import seaborn as sns


config = dotenv_values(".env")

seen = set()
actualAcceptanceArr = []
actualRejectionArr = []
with open("./data/selected_ids.txt", "r") as f:
    lines = f.readlines()
    for line in lines:
        arr = line.split(":")
        if (arr[0] in seen):
            continue             
        seen.add(arr[0].strip())
        if ("accept" in arr[1].lower()):
            actualAcceptanceArr.append(arr[0])
        else:
            actualRejectionArr.append(arr[0])


falseRejection = 0
falseAcceptance = 0
with open("./data/failures.txt", "r") as f:
    with psycopg.connect(config["DB_CONFIG"], row_factory=dict_row) as conn:
        falseAcceptanceArr = []
        falseRejectionArr = []

        with conn.cursor() as cur:
            lines = f.readlines()
            for line in lines:
                if (line.strip() not in seen):
                    continue
                
                seen.remove(line.strip())
                cur.execute('''SELECT * FROM metareviews WHERE id = %s''', [line.strip()])
                metareview = cur.fetchone()

                decision = metareview["decision"]
                if ("accept" in decision.lower()):
                    falseRejection += 1
                else:
                    falseAcceptance += 1

                cur.execute('''SELECT * FROM reviews WHERE id = %s''', [line.strip()])
                reviews = cur.fetchall()
                for review in reviews:
                    if ("accept" in decision.lower()):
                        falseRejectionArr.append(
                            { 
                                "ratings": review["rating"], 
                                "confidence": review["confidence"], 
                                "soundness": review["soundness"],
                                "presentation": review["presentation"],
                                "contribution": review["contribution"],
                            }
                        )
                    else:
                        falseAcceptanceArr.append(
                            { 
                                "ratings": review["rating"], 
                                "confidence": review["confidence"], 
                                "soundness": review["soundness"],
                                "presentation": review["presentation"],
                                "contribution": review["contribution"],
                            }
                        )
            
            print("False rejections: " + str(falseRejection))
            for stat in ["ratings", "confidence", "soundness", "presentation", "contribution"]:
                data = [x[stat] for x in falseRejectionArr]
                print(stat)
                average, std_dev = statistics.mean(data), statistics.stdev(data)

                print(f"Average: {average}")
                print(f"Standard Deviation: {std_dev}")
                print()
            
            print("False acceptances: " + str(falseAcceptance))
            for stat in ["ratings", "confidence", "soundness", "presentation", "contribution"]:
                data = [x[stat] for x in falseAcceptanceArr]
                print(stat)
                average, std_dev = statistics.mean(data), statistics.stdev(data)

                print(f"Average: {average}")
                print(f"Standard Deviation: {std_dev}")
                print()

            # Create confusion matrix
            total_samples = len(actualAcceptanceArr) + len(actualRejectionArr)
            true_positive = len(actualAcceptanceArr) - falseRejection
            true_negative = len(actualRejectionArr) - falseAcceptance
            
            cm = np.array([
                [true_positive, falseRejection],
                [falseAcceptance, true_negative]
            ])
            
            # Plot confusion matrix
            plt.figure(figsize=(8, 6))
            sns.heatmap(cm, annot=True, fmt='d', cmap='Blues',
                        xticklabels=['Predicted Accept', 'Predicted Reject'],
                        yticklabels=['Actual Accept', 'Actual Reject'])
            plt.title('Confusion Matrix')
            plt.ylabel('True Label')
            plt.xlabel('Predicted Label')
            plt.savefig('./data/cm/confusion_matrix.png')
            plt.close()