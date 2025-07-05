import os
import re
import shutil

# Directory containing the PNG files
source_dir = 'AI-Metareviewer/prompt_optimization/results/cm'

# First, move all PNG files from candidate folders back to main directory
for root, dirs, files in os.walk(source_dir):
    for file in files:
        if file.endswith('.png'):
            source_file = os.path.join(root, file)
            dest_file = os.path.join(source_dir, file)
            if root != source_dir:  # Only move if not in main directory
                shutil.move(source_file, dest_file)
                print(f'Moving back to main directory: {file}')

# Remove empty candidate folders
for item in os.listdir(source_dir):
    if item.startswith('candidate_'):
        folder_path = os.path.join(source_dir, item)
        if os.path.isdir(folder_path):
            try:
                os.rmdir(folder_path)
                print(f'Removed empty folder: {item}')
            except OSError:
                pass  # Folder not empty

# Regular expression to extract candidate number
pattern = r'candidate_(\d+)_'

# Get all PNG files and organize them
for filename in os.listdir(source_dir):
    if filename.endswith('.png'):
        # Extract candidate number
        match = re.search(pattern, filename)
        if match:
            candidate_num = match.group(1)
            # Create candidate directory if it doesn't exist
            candidate_dir = os.path.join(source_dir, f'candidate_{candidate_num}')
            os.makedirs(candidate_dir, exist_ok=True)
            
            # Move file to candidate directory
            source_file = os.path.join(source_dir, filename)
            dest_file = os.path.join(candidate_dir, filename)
            shutil.move(source_file, dest_file)
            print(f'Organized: Moved {filename} to {candidate_dir}') 