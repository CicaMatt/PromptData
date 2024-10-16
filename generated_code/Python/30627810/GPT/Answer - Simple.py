import re
import json

def parse_log_file(filename):
    log_entries = []
    current_entry = None

    # Regex pattern to match the log date at the beginning of the line
    log_pattern = re.compile(r"^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}")

    with open(filename, 'r') as f:
        for line in f:
            # Check if the line starts with a timestamp, indicating a new log entry
            if log_pattern.match(line):
                if current_entry:
                    # If there's an existing entry, add it to the list before starting a new one
                    log_entries.append(current_entry)

                # Split the log line into components (Date, Type, Message)
                parts = line.strip().split(" - ")
                if len(parts) >= 4:
                    current_entry = {
                        'Date': parts[0],
                        'Type': parts[2],
                        'Message': parts[3]
                    }
            else:
                # If the line does not start with a timestamp, it is part of the previous message
                if current_entry:
                    current_entry['Message'] += "\n" + line.strip()

    # Don't forget to append the last log entry
    if current_entry:
        log_entries.append(current_entry)

    return log_entries

# Convert log entries to JSON
def logs_to_json(log_entries, output_filename):
    with open(output_filename, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Example usage
log_file = 'logfile.log'
output_json = 'output_logs.json'

parsed_logs = parse_log_file(log_file)
logs_to_json(parsed_logs, output_json)

print(f"Logs have been parsed and saved to {output_json}")
