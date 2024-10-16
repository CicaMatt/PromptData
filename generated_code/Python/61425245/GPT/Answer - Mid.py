import requests
from bs4 import BeautifulSoup

# Credentials
username = '<USERNAME>'
password = '<PASSWORD>'

# URLs
login_url = 'https://sso.mims.com/Account/SignIn'
final_page_url = 'https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'

# Start a session
session = requests.Session()

# Headers to mimic a real browser
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

# Step 1: Get the login page to obtain hidden fields like authenticity tokens
login_page = session.get(login_url, headers=headers)
soup = BeautifulSoup(login_page.text, 'html.parser')

# Find and extract hidden fields required for login (if any)
hidden_inputs = soup.find_all("input", type="hidden")
login_data = {input_tag['name']: input_tag['value'] for input_tag in hidden_inputs}

# Add username and password to the login data
login_data['EmailAddress'] = username
login_data['Password'] = password

# Step 2: Post login credentials
response = session.post(login_url, data=login_data, headers=headers)

# Step 3: Handle redirection by checking the intermediate page (if exists)
# If the response does not directly take us to the target, manually handle the redirection
if "You will be redirected" in response.text:
    redirect_soup = BeautifulSoup(response.text, 'html.parser')
    hidden_form = redirect_soup.find('form')
    action_url = hidden_form['action']  # Find the redirect form action URL
    redirect_data = {input_tag['name']: input_tag['value'] for input_tag in hidden_form.find_all('input', type='hidden')}
    
    # Post the hidden form data to continue to the final destination
    response = session.post(action_url, data=redirect_data, headers=headers)

# Step 4: Now, we're logged in and redirected, access the target page
final_page = session.get(final_page_url, headers=headers)

# Step 5: Parse the final page to extract all links
soup = BeautifulSoup(final_page.content, 'html.parser')

# Collect all 'a' tags from the page
links = []
for a_tag in soup.find_all('a', href=True):
    links.append(a_tag['href'])

# Output the collected links
print(links)
