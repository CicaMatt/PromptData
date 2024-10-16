import requests
from bs4 import BeautifulSoup

# User credentials
USERNAME = 'your_username'
PASSWORD = 'your_password'

# Start a session
session = requests.Session()

# Set headers
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
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
if response.status_code == 200:
    print("Login successful")
else:
    print("Login failed")
    exit()

# URL to scrape
scrape_url = 'https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'

# Access the final page
response = session.get(scrape_url, headers=headers)

# Check if the page was accessed successfully
if response.status_code == 200:
    print("Page accessed successfully")
else:
    print("Failed to access the page")
    exit()

# Parse the page content
soup = BeautifulSoup(response.content, 'html.parser')

# Find all links
links = [a['href'] for a in soup.find_all('a', href=True)]

# Print the links
print(links)
