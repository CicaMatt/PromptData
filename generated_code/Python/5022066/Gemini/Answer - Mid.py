from sqlalchemy.orm import class_mapper
import json

def serialize_sqla_object(obj):
    """Serializes a SQLAlchemy object to a dictionary."""
    mapper = class_mapper(obj.__class__)
    columns = [column.key for column in mapper.columns]
    return {column: getattr(obj, column) for column in columns}

def serialize_sqla_query_result(result):
    """Serializes a SQLAlchemy query result to a list of dictionaries."""
    return [serialize_sqla_object(obj) for obj in result]

# Example usage:
query_result = session.query(Product).all()  # Replace 'Product' with your model
json_data = json.dumps(serialize_sqla_query_result(query_result))

# For XML serialization, consider using a library like `xmljson` or `dicttoxml`:
# import xmljson
# import xml.etree.ElementTree as ET
# xml_data = xmljson.badgerfish.etree(ET.fromstring(json_data))