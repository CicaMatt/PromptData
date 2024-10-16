import requests
from bs4 import BeautifulSoup

# Start a session
session = requests.Session()

# Login URL
login_url = 'https://sso.mims.com/Account/SignIn'

# Login credentials
payload = {
    'EmailAddress': '<USERNAME>',
    'Password': '<PASSWORD>'
}

# Headers
headers = {
    'User-Agent': 'Chrome'
}

# Login to the website
response = session.post(login_url, data=payload, headers=headers)

# Check if login was successful
if response.url == 'https://sso.mims.com/Account/SignIn':
    print("Login failed")
else:
    print("Login successful")

# Access the target URL
target_url = 'https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'
response = session.get(target_url, headers=headers)

# Parse the HTML content
soup = BeautifulSoup(response.content, 'html.parser')

# Find all the links
links = []
for a in soup.find_all('a', href=True):
    links.append(a['href'])

# Print the links
print(links)
