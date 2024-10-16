import re
import json

# Function to parse log files and convert to a list of JSON-like dicts
def parse_log_file_to_json(log_file):
    log_entries = []
    
    # Regular expression to match the start of a log entry (timestamp)
    log_entry_pattern = re.compile(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - .* - (INFO|ERROR|WARNING) - (.*)')

    current_entry = None

    with open(log_file, 'r') as file:
        for line in file:
            # Check if the line starts with a new log entry
            match = log_entry_pattern.match(line)
            if match:
                # If a new log entry starts, save the previous entry (if exists)
                if current_entry:
                    log_entries.append(current_entry)
                
                # Extract the date, type, and message from the match groups
                date, log_type, message = match.groups()
                
                # Create a new entry
                current_entry = {
                    'Date': date,
                    'Type': log_type,
                    'Message': message
                }
            else:
                # If the line doesn't match a new entry, it's a continuation of the previous message
                if current_entry:
                    # Append the line to the message (strip leading/trailing whitespace)
                    current_entry['Message'] += ' ' + line.strip()

        # Add the last entry after the loop ends
        if current_entry:
            log_entries.append(current_entry)

    return log_entries

# Function to save the parsed log entries to a JSON file or insert into a NoSQL database
def save_log_entries_to_json(log_entries, output_file):
    with open(output_file, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Example usage
log_file = 'log_file.log'  # Path to your log file
output_json = 'parsed_logs.json'  # Path to save the JSON output

# Parse the log file and get the log entries
log_entries = parse_log_file_to_json(log_file)

# Save the entries to a JSON file (can replace this with database logic)
save_log_entries_to_json(log_entries, output_json)

print("Log file successfully parsed and saved to JSON.")
