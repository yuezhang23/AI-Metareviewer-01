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

venue_group_settings = client.get_group(venue_id).content
submission_invitation = venue_group_settings['submission_id']['value']
submissions = client.get_all_notes(
    invitation=submission_invitation,
    details='directReplies'
)

fields = ['decision', 'comment']

venue_group_settings = client.get_group(venue_id).content
decision_invitation_name = venue_group_settings['decision_name']['value']
for submission in submissions:
    for reply in submission.details['directReplies']:
        if any(invitation.endswith(f'/-/{decision_invitation_name}') for invitation in reply['invitations']):
            with psycopg.connect(config["DB_CONFIG"]) as conn:
                with conn.cursor() as cur:
                    cur.execute("""
                    INSERT INTO metareviews (id, decision, comment)
                    VALUES (%s, %s, %s)
                    ON CONFLICT (id) DO UPDATE
                    SET decision = EXCLUDED.decision,
                        comment = EXCLUDED.comment;
                    """, 
                    (reply['replyto'], *[str(reply['content'][field]['value']) for field in fields]))

                    conn.commit()