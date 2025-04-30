import openreview
import psycopg
from dotenv import dotenv_values

config = dotenv_values(".env")

client = openreview.api.OpenReviewClient(
    baseurl='https://api2.openreview.net',
    username=config["OPENREVIEW_USERNAME"],
    password=config["OPENREVIEW_PASSWORD"]
)

venue_id = 'NeurIPS.cc/2024/Conference'

venue_group = client.get_group(venue_id)
submission_name = venue_group.content['submission_name']['value']
submissions = client.get_all_notes(invitation=f'{venue_id}/-/{submission_name}', details='replies')

review_name = venue_group.content['review_name']['value']
fields = ['summary', 'soundness', 'presentation', 'contribution', 'strengths', 'weaknesses', 'limitations', 'rating', 'confidence']

with psycopg.connect(config["DB_CONFIG"]) as conn:
    with conn.cursor() as cur:
        for s in submissions: 
            reviews=[openreview.api.Note.from_json(reply) for reply in s.details['replies'] if f'{venue_id}/{submission_name}{s.number}/-/{review_name}' in reply['invitations']]
            for r in reviews:

                cur.execute("""
                    INSERT INTO reviews (id, summary, soundness, presentation, contribution, strengths, weaknesses, limitations, rating, confidence)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                    """,
                    (r.replyto, *[str(r.content[field]['value']) for field in fields])
                )
                conn.commit()