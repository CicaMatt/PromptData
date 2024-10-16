import mechanize
import http.cookiejar as cookielib
from bs4 import BeautifulSoup

# Set up a browser object
br = mechanize.Browser()
cj = cookielib.LWPCookieJar()
br.set_cookiejar(cj)

br.set_handle_equiv(True)
br.set_handle_gzip(True)
br.set_handle_redirect(True)
br.set_handle_referer(True)
br.set_handle_robots(False)
br.set_handle_refresh(mechanize._http.HTTPRefreshProcessor(), max_time=1)

br.addheaders = [('User-agent', 'Chrome')]

# Open login page
br.open('https://sso.mims.com/Account/SignIn')

# Select the login form and fill in credentials
br.select_form(nr=0)
br.form['EmailAddress'] = '<USERNAME>'
br.form['Password'] = '<PASSWORD>'

# Submit login form
response = br.submit()

# Check if there's a middle page
html = response.read()

# Use BeautifulSoup to parse the page and find the hidden form
soup = BeautifulSoup(html, 'html.parser')
form = soup.find('form')

# Get form's action URL
form_action = form['action']

# Extract hidden input fields and values
hidden_inputs = {input_tag['name']: input_tag['value'] for input_tag in form.find_all('input', {'type': 'hidden'})}

# Manually submit the hidden form with the hidden inputs
br.open(form_action, data=hidden_inputs)

# Now access the final destination page
final_page = br.open('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand').read()

# Parse and extract the links
soup_final = BeautifulSoup(final_page, 'html.parser')

# Extract links from the final page
links = [a['href'] for a in soup_final.find_all('a', href=True)]

print(links)
