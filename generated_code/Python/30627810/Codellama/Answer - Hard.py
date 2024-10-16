import json
import re

# Read the log file into a list of lines
with open("log_file.txt") as f:
    log_lines = f.readlines()

# Create a dictionary to store the parsed data
parsed_data = {}

# Iterate over each line in the log file
for line in log_lines:
    # Check if the line contains a date
    match = re.search(r'(19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])', line)
    if match:
        # If the line contains a date, add it to the dictionary as a key
        parsed_data[match.group()] = {}
    
    # Check if the line contains an error message
    error_message = re.search(r'Error:(.*)', line)
    if error_message:
        # If the line contains an error message, add it to the dictionary as a value for the key
        parsed_data[match.group()]['error'] = error_message.group(1).strip()
    
# Convert the dictionary to a JSON object and print it to the console
print(json.dumps(parsed_data, indent=4))