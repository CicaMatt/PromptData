import json
import re

def parse_log_file(filename):
    """
    Parses a log file into a list of dictionaries, handling multi-line error messages.

    Args:
        filename (str): The name of the log file to parse.

    Returns:
        list: A list of dictionaries representing log entries.
    """

    log_entries = []
    current_entry = {}
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()  # Remove leading/trailing whitespace

            # Check for a new log entry based on date pattern
            match = re.match(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - .*? - (\w+) - (.*)', line)
            if match:
                if current_entry:  # Save the previous entry if it exists
                    log_entries.append(current_entry)

                # Start a new log entry
                current_entry = {
                    'Date': match.group(1),
                    'Type': match.group(2),
                    'Message': match.group(3)
                }
            else:
                # If it's not a new entry, it's part of the previous error message
                if current_entry:
                    current_entry['Message'] += '\n' + line 

    # Add the last entry
    if current_entry:
        log_entries.append(current_entry)

    return log_entries

def convert_to_json(log_entries):
    """
    Converts a list of log entry dictionaries to a JSON string.

    Args:
        log_entries (list): A list of dictionaries representing log entries.

    Returns:
        str: A JSON string representing the log entries.
    """
    return json.dumps(log_entries, indent=4)

# Example usage
filename = 'your_log_file.log'  # Replace with your actual log file name
log_entries = parse_log_file(filename)
json_data = convert_to_json(log_entries)

# Now you have 'json_data' ready to be loaded into your NoSQL database
print(json_data)