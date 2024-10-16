import requests
from bs4 import BeautifulSoup

# User credentials
USERNAME = '<USERNAME>'
PASSWORD = '<PASSWORD>'

# Start a session
session = requests.Session()

# Set the user-agent
headers = {
    'User-Agent': 'Chrome'
}

# Login URL
login_url = 'https://sso.mims.com/Account/SignIn'

# Login payload
login_payload = {
    'EmailAddress': USERNAME,
    'Password': PASSWORD
}

# Perform login
response = session.post(login_url, data=login_payload, headers=headers)

# Check if login was successful
if response.url == 'https://sso.mims.com/Account/SignIn':
    print("Login failed")
else:
    print("Login successful")

# URL to scrape
scrape_url = 'https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'

# Access the final page
response = session.get(scrape_url, headers=headers)

# Parse the page content
soup = BeautifulSoup(response.content, 'html.parser')

# Find all links
links = [a['href'] for a in soup.find_all('a', href=True)]

# Print the links
print(links)
