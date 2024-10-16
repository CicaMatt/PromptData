import json

str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
data = json.loads(str)

# Extract the "to" id from the JSON data
if 'to' in data:
    to_id = data['to']['data'][0]['id']
else:
    to_id = 'null'

print(to_id)