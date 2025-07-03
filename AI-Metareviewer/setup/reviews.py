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


def check_for_nul_bytes(values, fields):
    """Check which field contains NUL bytes and return the field name."""
    for i, value in enumerate(values):
        if isinstance(value, str) and '\x00' in value:
            return fields[i]
    return None

def clean_nul_bytes(value):
    """Remove NUL bytes from string values."""
    if isinstance(value, str):
        return value.replace('\x00', '')
    return value


config = dotenv_values(".env")

client = openreview.api.OpenReviewClient(
    baseurl='https://api2.openreview.net',
    username=config["OPENREVIEW_USERNAME"],
    password=config["OPENREVIEW_PASSWORD"]
)

year = 2023
venue_id = f'NeurIPS.cc/{year}/Conference'

# First get all valid submission IDs from metareviews table
with psycopg.connect(config["DB_CONFIG"]) as conn:
    with conn.cursor() as cur:
        cur.execute(f"SELECT id FROM metareviews_{year}_NeurIPS")
        valid_submission_ids = {row[0] for row in cur.fetchall()}

venue_group = client.get_group(venue_id)
submission_name = venue_group.content['submission_name']['value']
submissions = client.get_all_notes(invitation=f'{venue_id}/-/{submission_name}', details='replies')

# review_name = venue_group.content['review_name']['value']
fields = ['summary', 'soundness', 'presentation', 'contribution', 'strengths', 'weaknesses', 'questions', 'limitations', 'rating', 'confidence']
# extra_fields = ['rebuttal', 'comment']
numeric_fields = {'soundness', 'presentation', 'contribution', 'rating', 'confidence'}

with psycopg.connect(config["DB_CONFIG"]) as conn:
    with conn.cursor() as cur:
        for s in submissions:
            # Only process submissions that have metareviews
            if s.id not in valid_submission_ids:
                logger.info(f"Skipping submission {s.id} as it has no metareview")
                continue
                
            reviews=[openreview.api.Note.from_json(reply) for reply in s.details['replies']]            
            rebuttal_values = []
            official_values = []    
            comment_values = []
            for r in reviews:
                try:
                    # Try to get all required fields
                    if 'rebuttal' in r.content.keys():
                        rebuttal_values.append({'r_id' : r.id, 'reply_id' : r.replyto, 'rebuttal' : r.content['rebuttal']['value']})
                    elif 'comment' in r.content.keys():
                        comment_values.append({'c_id' : r.id, 'reply_id' : r.replyto, 'comment' : r.content['comment']['value']})                   
                    elif 'decision' not in r.content.keys():
                        values = []
                        if r.replyto == s.id:
                            for field in fields:
                                try:
                                    value = r.content[field]['value']
                                    if field in numeric_fields:
                                        value = extract_numeric_value(value)
                                    elif value is None:
                                        value = 'not_provided'
                                    else:
                                        value = str(value)
                                    # Clean NUL bytes from all string values
                                    value = clean_nul_bytes(value)
                                except (KeyError, TypeError):
                                    logger.warning(f"Missing field '{field}' in review {r.replyto}, skipping this review")
                                    raise KeyError(f"Missing field: {field}")                  
                                values.append(value)
                            official_values.append({'s_id' : r.id, 'values' : values, 'rebuttal' : ''})
                    # Skip reviews that have a 'decision' field (these are not actual reviews)
                    else:
                        break
                except KeyError as e:
                    logger.error(f"Error processing review {r.replyto}: {str(e)}")
                    continue  
                except Exception as e:
                    logger.error(f"Unexpected error processing review {r.replyto}: {str(e)}")
                    logger.error(f"Error type: {type(e).__name__}")
                    continue  

            # dfs to link comments
            def link_comments(comment_values, id, accr_values):
                for cc in comment_values:
                    if cc['reply_id'] == id:
                        accr_values += '\n\nReply:\n' + cc['comment']
                        accr_values = link_comments(comment_values, cc['c_id'], accr_values)
                        break
                return accr_values
                          
            # Add comments to rebuttals
            for rebut_data in rebuttal_values:
                for comment_data in comment_values:
                    if comment_data['reply_id'] == rebut_data['r_id']: 
                        accr_values = link_comments(comment_values, comment_data['c_id'], '')
                        rebut_data['rebuttal'] += '\n\nComment:\n' + comment_data['comment'] + accr_values

            # Add rebuttals and comments to official reviews
            for official_data in official_values:
                for rebuttal_data in rebuttal_values:
                    if rebuttal_data['reply_id'] == official_data['s_id']:
                        official_data['rebuttal'] += rebuttal_data['rebuttal']
                        # Clean NUL bytes from rebuttal text
                for comment_data in comment_values: 
                    if comment_data['reply_id'] == official_data['s_id']:
                        accr_values = link_comments(comment_values, comment_data['c_id'], '')
                        official_data['rebuttal'] += '\n\nComment:\n' + comment_data['comment'] + accr_values
                official_data['rebuttal'] = clean_nul_bytes(official_data['rebuttal'])          
                
                try:
                    cur.execute(f"""
                        INSERT INTO reviews_{year}_NeurIPS (id, summary, soundness, presentation, contribution, strengths, weaknesses, questions, limitations, rating, confidence, rebuttal)
                            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                        """,
                        (s.id, *official_data['values'], official_data['rebuttal']))     
                    conn.commit()
                except Exception as e:
                    logger.error(f"Error inserting review {s.id}: {str(e)}")
                    continue

            for rebuttal_data in rebuttal_values:
                if rebuttal_data['reply_id'] == s.id:
                    values_p = [None] * len(fields)
                    rebuttal_text = rebuttal_data['rebuttal']
                    rebuttal_text = clean_nul_bytes(rebuttal_text) 
                    try:
                        cur.execute(f"""
                        INSERT INTO reviews_{year}_NeurIPS (id, summary, soundness, presentation, contribution, strengths, weaknesses, questions, limitations, rating, confidence, rebuttal)
                            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                            """,
                            (s.id, *values_p, rebuttal_text)) 
                        conn.commit()
                    except Exception as e:
                        logger.error(f"Error inserting rebuttal {s.id}: {str(e)}")
                        continue
