import re
import json

def parse_log_file(filename):
    """
    Parses the log file and returns a list of JSON objects representing each log entry.

    Args:
        filename: The name of the log file to parse.

    Returns:
        A list of dictionaries, where each dictionary represents a log entry.
    """
    log_entries = []
    with open(filename, 'r') as f:
        current_entry = {}
        for line in f:
            line = line.strip()  # Remove leading/trailing whitespace

            # Check if the line starts with a date (indicating a new log entry)
            date_match = re.match(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3})', line)
            if date_match:
                # If we have a current entry, append it to the list before starting a new one
                if current_entry:
                    log_entries.append(current_entry)

                # Start a new log entry
                current_entry = {
                    'Date': date_match.group(1),
                    'Type': line.split(' - ')[2],  # Extract the log type
                    'Message': line[date_match.end():].strip()  # Extract the message
                }
            else:
                # If it's not a new entry, it's a continuation of the previous message
                if current_entry:
                    current_entry['Message'] += '\n' + line

        # Append the last entry if it exists
        if current_entry:
            log_entries.append(current_entry)

    return log_entries

# Example usage
filename = 'your_log_file.log'  # Replace with your actual log file name
log_data = parse_log_file(filename)

# Convert to JSON
json_data = json.dumps(log_data, indent=4)

# Print the JSON data (or you can load it into your NoSQL database)
print(json_data)