import json

str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
data = json.loads(str)

post_id = data['id']
post_type = data['type']
print(post_id)
print(post_type)

created_time = data['created_time']
updated_time = data['updated_time']
print(created_time)
print(updated_time)

if 'to' in data:
    to_id = data['to']['data'][0]['id']
    print('To id:', to_id)
else:
    print('null')