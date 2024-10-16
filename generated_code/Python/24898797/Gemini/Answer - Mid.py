import json

str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
data = json.loads(str)

post_id = data['id']
post_type = data['type']
print(post_id)  # Output: id_7
print(post_type)  # Output: status

created_time = data['created_time']
updated_time = data['updated_time']
print(created_time)  # Output: 2012-05-01
print(updated_time)  # Output: 2012-05-01

if data.get('application'):
    app_id = data['application'].get('id', 0)
    print(app_id)
else:
    print('null')  # Output: null

# Extracting the 'to_id'
if data.get('to') and data['to'].get('data'):
    to_id = data['to']['data'][0].get('id')  # Access the first element in the 'data' list
    print(to_id)  # Output: 1543
else:
    print('null')