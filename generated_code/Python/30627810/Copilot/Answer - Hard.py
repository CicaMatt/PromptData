import re
import json

def parse_log_file(filename):
    log_entries = []
    log_entry = {}
    log_pattern = re.compile(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) - \S+ - (\w+) - (.*)')

    with open(filename, 'r') as file:
        lines = file.readlines()

    for line in lines:
        match = log_pattern.match(line)
        if match:
            if log_entry:
                log_entries.append(log_entry)
            log_entry = {
                'Date': match.group(1),
                'Type': match.group(2),
                'Message': match.group(3)
            }
        else:
            if log_entry:
                log_entry['Message'] += '\n' + line.strip()

    if log_entry:
        log_entries.append(log_entry)

    return log_entries

def save_to_json(log_entries, output_filename):
    with open(output_filename, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Example usage
log_entries = parse_log_file('logfile.txt')
save_to_json(log_entries, 'logfile.json')
