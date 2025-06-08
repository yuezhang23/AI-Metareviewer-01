import pandas as pd
import sys
from typing import Set, Tuple, Dict
from collections import Counter

def read_csv_ids(file_path: str) -> Set[str]:
    try:
        # Read CSV with semicolon separator
        df = pd.read_csv(file_path, sep=';')
        
        # Check if 'id' column exists
        if 'id' not in df.columns:
            raise ValueError(f"Column 'id' not found in {file_path}")
            
        # Convert IDs to set
        return set(df['id'].astype(str))
    except FileNotFoundError:
        raise FileNotFoundError(f"File not found: {file_path}")
    except pd.errors.EmptyDataError:
        raise pd.errors.EmptyDataError(f"File is empty: {file_path}")
    except pd.errors.ParserError:
        raise pd.errors.ParserError(f"Invalid CSV format in {file_path}")

def compare_csv_ids(file1_path: str, file2_path: str) -> Tuple[Set[str], Set[str], Set[str]]:
    # Read IDs from both files
    ids1 = read_csv_ids(file1_path)
    ids2 = read_csv_ids(file2_path)
    
    # Find differences and common IDs
    only_in_file1 = ids1 - ids2
    only_in_file2 = ids2 - ids1
    common_ids = ids1 & ids2
    
    return only_in_file1, only_in_file2, common_ids

def check_duplicate_ids(file_path: str) -> Dict[str, int]:
    """
    Check for duplicate IDs in a CSV file.
    
    Args:
        file_path (str): Path to the CSV file
        
    Returns:
        Dict[str, int]: Dictionary containing duplicate IDs as keys and their counts as values
        Returns empty dict if no duplicates found
    """
    try:
        # Read CSV with semicolon separator
        df = pd.read_csv(file_path, sep=';')
        
        # Check if 'id' column exists
        if 'id' not in df.columns:
            raise ValueError(f"Column 'id' not found in {file_path}")
            
        # Convert IDs to string and count occurrences
        id_counts = Counter(df['id'].astype(str))
        
        # Filter only duplicates (count > 1)
        duplicates = {id: count for id, count in id_counts.items() if count > 1}
        
        return duplicates
    except FileNotFoundError:
        raise FileNotFoundError(f"File not found: {file_path}")
    except pd.errors.EmptyDataError:
        raise pd.errors.EmptyDataError(f"File is empty: {file_path}")
    except pd.errors.ParserError:
        raise pd.errors.ParserError(f"Invalid CSV format in {file_path}")

def main():
    if len(sys.argv) < 2:
        print("Usage:")
        print("  For comparing two files: python compare_csv_ids.py <file1.csv> <file2.csv>")
        print("  For checking duplicates: python compare_csv_ids.py --check-duplicates <file.csv>")
        sys.exit(1)
    
    # Check if we're looking for duplicates
    if sys.argv[1] == "--check-duplicates":
        if len(sys.argv) != 3:
            print("Usage: python compare_csv_ids.py --check-duplicates <file.csv>")
            sys.exit(1)
            
        file_path = sys.argv[2]
        try:
            duplicates = check_duplicate_ids(file_path)
            
            if duplicates:
                print(f"\nFound {len(duplicates)} duplicate IDs in {file_path}:")
                for id, count in sorted(duplicates.items()):
                    print(f"  ID: {id} appears {count} times")
            else:
                print(f"\nNo duplicate IDs found in {file_path}")
                
        except Exception as e:
            print(f"Error: {str(e)}")
            sys.exit(1)
    else:
        # Original comparison logic
        if len(sys.argv) != 3:
            print("Usage: python compare_csv_ids.py <file1.csv> <file2.csv>")
            sys.exit(1)
            
        file1_path = sys.argv[1]
        file2_path = sys.argv[2]
        
        try:
            only_in_file1, only_in_file2, common_ids = compare_csv_ids(file1_path, file2_path)        
            # Print results
            print(f"\nComparison Results:")
            print(f"Total IDs in {file1_path}: {len(only_in_file1) + len(common_ids)}")
            print(f"Total IDs in {file2_path}: {len(only_in_file2) + len(common_ids)}")
            print(f"Common IDs: {len(common_ids)}")
            print(f"IDs only in {file1_path}: {len(only_in_file1)}")
            print(f"IDs only in {file2_path}: {len(only_in_file2)}")
                    
        except Exception as e:
            print(f"Error: {str(e)}")
            sys.exit(1)

if __name__ == "__main__":
    main() 