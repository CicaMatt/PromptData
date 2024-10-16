import requests
from bs4 import BeautifulSoup

# Enter URL to be scraped here
url = "https://www.example.com"

# Initialize session
session = requests.Session()

# Get HTML content of website
response = session.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

# Print all links found on the webpage
print([link.get('href') for link in soup.find_all('a')])