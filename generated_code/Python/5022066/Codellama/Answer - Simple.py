from flask import jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# Connect to database
engine = create_engine('sqlite:///mydatabase.db')
Session = sessionmaker(bind=engine)
session = Session()

# Query the database and get the results as a list of ORM objects
products = session.query(Product).all()

# Serialize the results to JSON
json_data = jsonify(products)

print(json_data)