import openreview
import psycopg
from dotenv import dotenv_values
import logging
import re

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def extract_numeric_value(value):
    """Extract numeric value from text like '2 fair' or '3 good'."""
    if isinstance(value, (int, float)):
        return str(value)
    # Try to find a number at the start of the string
    match = re.match(r'^\s*(\d+)', str(value))
    if match:
        return match.group(1)
    return str(value)

config = dotenv_values("../../.env")


client = openreview.api.OpenReviewClient(
    baseurl='https://api2.openreview.net',
    username=config["OPENREVIEW_USERNAME"],
    password=config["OPENREVIEW_PASSWORD"]
)

venue_id = 'NeurIPS.cc/2024/Conference'

venue_group = client.get_group(venue_id)
submission_name = venue_group.content['submission_name']['value']
submissions = client.get_all_notes(invitation=f'{venue_id}/-/{submission_name}',details='replies')

review_name = venue_group.content['review_name']['value']

# reply_type = "Official_Review" #also: "Meta_Review","Official_Comment", "Decision", "Rebuttal" etc.
rebuttal_count = 0
for s in submissions:
    if s.id == "zxSWIdyW3A":
        reviews=[openreview.api.Note.from_json(reply) for reply in s.details['replies']]   
        for i, r in enumerate(reviews):
            if ('decision' in r.content.keys()):
                print(f"{r.id} from decision: {r.replyto}\n")
                print(r.content['decision']['value'])
            elif ('comment' in r.content.keys()):
                print(f"{r.id} from comment: {r.replyto}\n")
                print(r.content['comment']['value'])
            elif ('rebuttal' in r.content.keys()):
                print(f"{r.id} from rebuttal: {r.replyto}\n")
                print(r.content['rebuttal']['value'])
            else:
                print(f"{r.id} from official_review to {r.replyto}\n")
        break


    # for r in reviews:
    #     try:
    #         # Try to get all required fields
    #         values = []
    #         for field in fields:
    #             try:
    #                 value = r.content[field]['value']
    #                 # For numeric fields, extract the numeric part but keep as string
    #                 if field in numeric_fields:
    #                     value = extract_numeric_value(value)
    #                 else:
    #                     value = str(value)
    #             except (KeyError, TypeError):
    #                 if field == 'limitations':  # Make limitations optional
    #                     value = None
    #                 else:
    #                     logger.warning(f"Missing field '{field}' in review {r.replyto}, skipping this review")
    #                     raise KeyError(f"Missing field: {field}")
    #             values.append(value)
            
    #     except KeyError as e:
    #         logger.error(f"Error processing review {r.replyto}: {str(e)}")
    #         continue  # Skip this review and continue with the next one
    #     except Exception as e:
    #         logger.error(f"Unexpected error processing review {r.replyto}: {str(e)}")
    #         continue  # Skip this review and continue with the next one