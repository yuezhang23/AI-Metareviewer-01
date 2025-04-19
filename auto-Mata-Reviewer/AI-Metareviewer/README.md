# Introduction

This is a WIP project with a goal of analyzing whether an LLM like ChatGPT can accurately predict whether a paper submitted to an academic conference (ICLR) would be accepted. The LLM is provided reviews of the paper, then it must act as the "metareviewer" and ultimately make a decision.

Author: David Zhu

With support from Andrew Bai

# Overview of Project Setup

The project workflow has a setup phase that must be run only once and a main phase. The data of reviews and metareviews are taken from the [ICLR API](https://docs.openreview.net/) and placed into a database for fast access and easy querying. I have used Postgres + Docker for this. To set one up yourself, check out [this](https://www.dbvis.com/thetable/how-to-set-up-postgres-using-docker/). Stop at Step 4, then execute 

`docker exec -it postgres_container su - postgres`

 This opens an interactive session to the container, and then you can use `psql` to begin interacting with the database. Once in, create a database with 

 `CREATE DATABASE iclr_reviews`

 and use `\c` to connect to it. Then set up the connection to the database in a `.env` file: 

 `DB_CONFIG = "dbname=iclr_reviews user=postgres password=<password> host=localhost port=5432"`

 You should now be able to run `/setup/db_init.py`! This will create the tables needed for the data.

 To run `/setup/reviews.py`, you need to create an account for OpenReview [here](https://docs.openreview.net/getting-started/creating-an-openreview-profile/signing-up-for-openreview), then configure `OPENREVIEW_USERNAME` and `OPENREVIEW_PASSWORD` in `.env`.

 # Explanation of Other Files
 `prompt_gpt.py` and `prompt_gemini.py` contain the architecture used to pass the data to the LLM and ask for its response. You will likely be changing the wording of the prompt often. Note that you should add `OPENAI_API_KEY` to `.env`. 

 `utility.py` contains some qualitative defintiions for the scoring that ICLR uses to give more context to the LLM. It also contains a rudimentary benchmark to compare the LLM's performance with; the benchmark just averages all ratings and accepts the paper if the average rating is > 5. 

 `/data` contains some data I have collected. `failures.txt` is a log of all paper id's for which the LLM output a wrong result. `hypotheses.txt` contains a rough log of previous parameters tested and the results.

 `failure_analysis.py` contains some statistical analysis on the `failures.txt` file. 

 `main.py` is where most work will be done, and it contains the infrastructure for picking a paper from the conference, aggregating the reviews, and asking the LLM to accept or reject it based on the reviews. It then compares the LLM's answer to the actual result. How you query for papers can and should change; for example, you choose only papers that have been rejected, only papers with an average rating > 7, etc. to test the LLM on these subcases. `main.py` is the file you should run, and it will output the performance of the LLM on the subset of data you choose. 

 `prompt_optimization` is a complicated add-on to try and improve the performance of the LLM through prompt engineering (I did not write this, credits to the author of [this paper](https://arxiv.org/abs/2305.03495)). I have tweaked it for our purposes; check out the README in that folder, which contains the specific command to run. Note that you must provide data to it in the `/data` folder, where you will see `metareviewer_data.csv` already provided. The format of the csv should be one column for paper `id`, one containing all the reviews for the paper, and a label (0 or 1) indicating whether the paper was accepted. This is quite costly to run so use sparingly. Also note that this folder contains a lot of things on ethos, liar, etc. These are tests that the researchers who wrote the paper ran, and are not relevant to us. 