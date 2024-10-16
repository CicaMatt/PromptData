import re
import json

def parse_log_file(filename):
    log_entries = []
    log_entry = {}
    log_message = []

    date_pattern = re.compile(r'^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}')
    log_level_pattern = re.compile(r' - (INFO|ERROR|DEBUG|WARNING) - ')

    with open(filename, 'r') as file:
        for line in file:
            if date_pattern.match(line):
                if log_entry:
                    log_entry['Message'] = ''.join(log_message).strip()
                    log_entries.append(log_entry)
                    log_entry = {}
                    log_message = []

                date_match = date_pattern.match(line)
                log_level_match = log_level_pattern.search(line)

                if date_match and log_level_match:
                    log_entry['Date'] = date_match.group(0)
                    log_entry['Type'] = log_level_match.group(1)
                    log_message.append(line[log_level_match.end():].strip())
            else:
                log_message.append(line.strip())

        if log_entry:
            log_entry['Message'] = ''.join(log_message).strip()
            log_entries.append(log_entry)

    return log_entries

def save_to_json(log_entries, output_filename):
    with open(output_filename, 'w') as json_file:
        json.dump(log_entries, json_file, indent=4)

# Example usage
log_entries = parse_log_file('logfile.log')
save_to_json(log_entries, 'logfile.json')
