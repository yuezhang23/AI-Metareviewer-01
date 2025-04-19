FROM python:3.9-slim

# Install system dependencies required for psycopg2 and matplotlib
RUN apt-get update && apt-get install -y \
    gcc \
    libpq-dev \
    python3-tk \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app/

# Create data directory and set permissions
RUN mkdir -p /app/data && \
    chmod 777 /app/data

# Define volume for data directory
VOLUME ["/app/data"]

# Copy requirements first to leverage Docker cache
COPY requirement.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirement.txt

# Copy the rest of the application
COPY . .

# Set environment variables
ENV PYTHONPATH=/app
