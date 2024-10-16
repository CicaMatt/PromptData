import re
import json

def parse_log_file(filename):
    with open(filename, 'r') as file:
        log_data = file.read()

    # Regular expression to match log entries
    log_entry_pattern = re.compile(
        r'(?P<Date>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - __main__ - (?P<Type>\w+) - (?P<Message>.*?)(?=\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3} - __main__ - |\Z)',
        re.DOTALL
    )

    log_entries = []
    for match in log_entry_pattern.finditer(log_data):
        log_entry = match.groupdict()
        log_entries.append(log_entry)

    return log_entries

def save_to_json(log_entries, output_filename):
    with open(output_filename, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Example usage
log_entries = parse_log_file('logfile.log')
save_to_json(log_entries, 'logfile.json')
