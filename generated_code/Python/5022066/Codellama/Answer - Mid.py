from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import json
import xml.etree.ElementTree as ET

# Connect to the database and create a session
engine = create_engine('sqlite:///mydatabase.db')
Session = sessionmaker(bind=engine)
session = Session()

# Query the database and get the results
products = session.query(Product).all()

# Convert the query result to JSON
json_data = json.dumps(products, cls=JSONEncoder)
print(json_data)

# Convert the query result to XML
xml_data = ET.tostring(products, encoding='utf-8')
print(xml_data)
