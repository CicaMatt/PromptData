import re
import json

def parse_log_file(log_file_path):
    log_entries = []
    log_entry = None
    
    # Regex pattern to match the start of a new log entry
    log_entry_pattern = re.compile(r'^(?P<date>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - .* - (?P<type>INFO|ERROR|DEBUG|WARNING) - (?P<message>.*)')
    
    with open(log_file_path, 'r') as log_file:
        for line in log_file:
            # Check if this line starts a new log entry
            match = log_entry_pattern.match(line)
            
            if match:
                # If there's an ongoing log entry, finalize and store it
                if log_entry:
                    log_entries.append(log_entry)
                
                # Start a new log entry
                log_entry = {
                    'Date': match.group('date'),
                    'Type': match.group('type'),
                    'Message': match.group('message').strip()
                }
            else:
                # This line is part of the message of the current log entry (e.g., part of a stack trace)
                if log_entry:
                    log_entry['Message'] += '\n' + line.strip()

        # Append the last log entry after processing the file
        if log_entry:
            log_entries.append(log_entry)
    
    return log_entries

def convert_logs_to_json(log_entries, output_json_path):
    with open(output_json_path, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Usage
log_file_path = 'your_log_file.log'
output_json_path = 'output_logs.json'

# Parse the log file into structured log entries
parsed_log_entries = parse_log_file(log_file_path)

# Convert the parsed log entries into a JSON file
convert_logs_to_json(parsed_log_entries, output_json_path)

print(f"Log entries successfully converted to {output_json_path}")
