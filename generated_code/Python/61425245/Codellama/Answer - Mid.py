from bs4 import BeautifulSoup
import mechanize

# Use mechanize to navigate to the login page and submit the form
br = mechanize.Browser()
cj = cookielib.LWPCookieJar()
br.set_cookiejar(cj)
br.open('https://sso.mims.com/Account/SignIn')
br.select_form(nr=0)
br['EmailAddress'] = "<USERNAME>"
br['Password'] = "<PASSWORD>"
response = br.submit()

# Use BeautifulSoup to parse the HTML of the middle page and locate the hidden form
soup = BeautifulSoup(response, 'html.parser')
hidden_form = soup.find('form', {'action': '/Account/SignIn'})

# Submit the hidden form programmatically using mechanize
br.submit(hidden_form)

# Use the new response object to access the contents of the end page
end_page = br.open('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand')
print(end_page.read())