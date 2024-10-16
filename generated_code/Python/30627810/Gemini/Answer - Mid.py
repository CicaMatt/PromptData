import re
import json

def parse_log_file(filename):
    log_entries = []
    with open(filename, 'r') as f:
        current_entry = {}
        for line in f:
            line = line.strip()  # Remove leading/trailing whitespace

            # Check for a new log entry based on date pattern
            match = re.match(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - .*? - (\w+) - (.*)', line)
            if match:
                if current_entry:  # Save previous entry if it exists
                    log_entries.append(current_entry)
                current_entry = {
                    'Date': match.group(1),
                    'Type': match.group(2),
                    'Message': match.group(3)
                }
            else:  # Append to the current message if it's a continuation
                if current_entry:
                    current_entry['Message'] += '\n' + line

        if current_entry:  # Save the last entry
            log_entries.append(current_entry)

    return log_entries

# Example usage
filename = 'your_log_file.txt'  # Replace with your actual log file
log_data = parse_log_file(filename)

# Convert to JSON
json_data = json.dumps(log_data, indent=4)

# Print or save the JSON data
print(json_data)