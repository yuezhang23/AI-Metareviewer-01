import pandas as pd
import re
import os

def parse_hypotheses_file(file_path):
    with open(file_path, 'r') as f:
        content = f.read()

    # Split into sections
    sections = content.split('---')
    results = []
    
    current_note = ""
    for section in sections:
        if not section.strip():
            continue
            
        # Extract note if present
        if "note:" in section.lower():
            current_note = section.split("note:")[1].strip()
            continue
            
        # Extract test description
        test_match = re.search(r'Test: (.*?)(?=\n|$)', section)
        if not test_match:
            continue
        test_desc = test_match.group(1).strip()
        
        # Extract metrics
        metrics = {
            'Total Accepted': None,
            'Total Rejected': None,
            'Success Rate on Accept': None,
            'Benchmark Success Rate on Accept': None,
            'Success Rate on Reject': None,
            'Benchmark Success Rate on Reject': None,
            'F1 Score': None
        }
        
        for metric in metrics.keys():
            match = re.search(f'{metric}: ([\d.]+)', section)
            if match:
                metrics[metric] = float(match.group(1))
        
        results.append({
            'Test Description': test_desc,
            'Note': current_note,
            **metrics
        })
    
    return pd.DataFrame(results)

def main():
    # Get the directory of the current script
    script_dir = os.path.dirname(os.path.abspath(__file__))
    data_dir = os.path.join(script_dir, 'data')
    
    # Parse the hypotheses file
    df = parse_hypotheses_file(os.path.join(data_dir, 'hypotheses.txt'))
    
    # Create Excel writer
    output_file = os.path.join(data_dir, 'hypotheses_summary.xlsx')
    with pd.ExcelWriter(output_file, engine='openpyxl') as writer:
        # Write main results
        df.to_excel(writer, sheet_name='Detailed Results', index=False)
        
        # Create summary statistics
        summary = pd.DataFrame({
            'Metric': [
                'Average Success Rate on Accept',
                'Average Benchmark Success Rate on Accept',
                'Average Success Rate on Reject',
                'Average Benchmark Success Rate on Reject',
                'Average F1 Score'
            ],
            'Value': [
                df['Success Rate on Accept'].mean(),
                df['Benchmark Success Rate on Accept'].mean(),
                df['Success Rate on Reject'].mean(),
                df['Benchmark Success Rate on Reject'].mean(),
                df['F1 Score'].mean()
            ]
        })
        summary.to_excel(writer, sheet_name='Summary Statistics', index=False)
        
        # Create comparison of different prompt versions
        prompt_versions = df[df['Test Description'].str.contains('prompt', case=False, na=False)]
        prompt_versions.to_excel(writer, sheet_name='Prompt Comparisons', index=False)

if __name__ == '__main__':
    main() 