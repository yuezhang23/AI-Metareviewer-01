import psycopg
from dotenv import dotenv_values

config = dotenv_values(".env")
# with psycopg.connect("dbname=iclr_reviews user=postgres password=pass host=db port=5432") as conn:
with psycopg.connect(config["DB_CONFIG"]) as conn:
    with conn.cursor() as cur:
        cur.execute("""
                CREATE TABLE IF NOT EXISTS metareviews_2023_NeurIPS (
                id VARCHAR(25) PRIMARY KEY,
                decision TEXT,
                comment TEXT NULL
            );""")
        cur.execute("""
                CREATE TABLE IF NOT EXISTS reviews_2023_NeurIPS (
                id VARCHAR(25) REFERENCES metareviews_2023_NeurIPS(id),
                summary TEXT,
                soundness TEXT,
                presentation TEXT,
                contribution TEXT,
                strengths TEXT,
                weaknesses TEXT,
                questions TEXT,
                limitations TEXT NULL, 
                rating TEXT,
                confidence TEXT
                );""")
        conn.commit()