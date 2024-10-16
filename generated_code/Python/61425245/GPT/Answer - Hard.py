import requests
from bs4 import BeautifulSoup

# Credentials for logging in
USERNAME = 'your_username'
PASSWORD = 'your_password'

# URLs for login and target page
login_url = 'https://sso.mims.com/Account/SignIn'
target_url = 'https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'

# Create a session to handle cookies and persist login
session = requests.Session()

# Define headers to mimic a browser request
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}

# First, fetch the login page to get any hidden tokens (like CSRF tokens)
login_page = session.get(login_url, headers=headers)
soup = BeautifulSoup(login_page.text, 'html.parser')

# Assuming the login form uses hidden CSRF token, retrieve it (if applicable)
# For example, if there's a hidden input field for the token:
csrf_token = soup.find('input', {'name': 'RequestVerificationToken'})
token_value = csrf_token['value'] if csrf_token else None

# Prepare payload for login
payload = {
    'EmailAddress': USERNAME,
    'Password': PASSWORD,
    'RequestVerificationToken': token_value  # Add token if required
}

# Perform login
response = session.post(login_url, headers=headers, data=payload)

# Check if login was successful by inspecting the response or session state
if response.status_code == 200 and "Logout" in response.text:
    print("Login successful!")
else:
    print("Login failed. Please check your credentials.")

# Now access the target page
target_response = session.get(target_url, headers=headers)

# Check if the redirection page is present
if "You will be redirected" in target_response.text:
    print("Handling intermediate redirection...")
    # BeautifulSoup to extract the form and submit it automatically
    redirect_soup = BeautifulSoup(target_response.text, 'html.parser')
    redirect_form = redirect_soup.find('form')
    if redirect_form:
        action_url = redirect_form['action']
        form_data = {input_tag['name']: input_tag.get('value', '') for input_tag in redirect_form.find_all('input')}
        # Submit the form to complete the redirection
        redirect_response = session.post(action_url, data=form_data, headers=headers)
        final_page = redirect_response.text
    else:
        print("Failed to find redirection form.")
        final_page = target_response.text
else:
    # No intermediate page, we are already on the target page
    final_page = target_response.text

# Parse the final page and extract all the links
soup = BeautifulSoup(final_page, 'html.parser')

# Find all the anchor tags with href attributes (links)
links = []
for link in soup.find_all('a', href=True):
    links.append(link['href'])

# Display the collected links
print(f"Collected {len(links)} links from the page.")
for idx, link in enumerate(links):
    print(f"{idx + 1}: {link}")
