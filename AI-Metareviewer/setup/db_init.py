import psycopg
from dotenv import dotenv_values

config = dotenv_values(".env")
# with psycopg.connect("dbname=iclr_reviews user=postgres password=pass host=db port=5432") as conn:
with psycopg.connect(config["DB_CONFIG"]) as conn:
    with conn.cursor() as cur:
        cur.execute("""
            CREATE TABLE IF NOT EXISTS metareviews (
                id VARCHAR(25) PRIMARY KEY,
                decision TEXT,
                comment TEXT
            );""")
        cur.execute("""
            CREATE TABLE IF NOT EXISTS reviews (
                id VARCHAR(25) REFERENCES metareviews(id),
                summary TEXT,
                soundness INTEGER,
                presentation INTEGER,
                contribution INTEGER,
                strengths TEXT,
                weaknesses TEXT,
                questions TEXT,
                limitations TEXT, 
                rating INTEGER,
                confidence INTEGER
                );""")
        conn.commit()