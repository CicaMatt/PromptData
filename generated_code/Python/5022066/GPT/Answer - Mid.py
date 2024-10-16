import json
from sqlalchemy.orm import class_mapper

# Function to serialize SQLAlchemy ORM object to a dictionary
def to_dict(obj):
    """
    Convert SQLAlchemy object to a dictionary by extracting its columns and their values.
    """
    return {column.key: getattr(obj, column.key) for column in class_mapper(obj.__class__).columns}

# Function to serialize a list of SQLAlchemy objects
def serialize_query_result(query_result):
    """
    Converts SQLAlchemy query result to a list of dictionaries.
    """
    return [to_dict(item) for item in query_result]

# Example usage:
# Assuming you have a Product model and a query result from SQLAlchemy
# query_result = session.query(Product).all()

# Serialize the result to JSON
json_result = json.dumps(serialize_query_result(query_result), indent=4)

# Output the JSON result
print(json_result)
