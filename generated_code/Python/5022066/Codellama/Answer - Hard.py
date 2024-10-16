from dict2xml import dict2xml
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, create_engine

Base = declarative_base()

class Product(Base):
    __tablename__ = 'product'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    description = Column(String)

# Creazione dell'engine
engine = create_engine('sqlite:///db.sqlite')
Session = sessionmaker(bind=engine)
session = Session()

# Query sul database e ottenimento dei risultati
products = session.query(Product).all()

# Conversione dei risultati della query in dizionario
product_dict = {product.id: {'name': product.name, 'description': product.description} for product in products}

# Conversione del dizionario in XML usando dict2xml
xml = dict2xml(product_dict)

print(xml)
