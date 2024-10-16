import re

with open('log_file.txt', 'r') as f:
    log_lines = [line for line in f]

for line in log_lines:
    match = re.search(r'^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d+).*?\s*(INFO|ERROR)\s*\-.*?:(?P<message>.*)$', line)
    if match:
        date = match.group(1)
        type = match.group(2)
        message = match.group('message')

        print(date, type, message)