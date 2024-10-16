import mechanize
import http.cookiejar as cookielib
from bs4 import BeautifulSoup
import html2text

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

# Open the login page
br.open('https://sso.mims.com/Account/SignIn')

# View available forms
for f in br.forms():
    print(f)

br.select_form(nr=0)

# User credentials
br.form['EmailAddress'] = "<USERNAME>"
br.form['Password'] = "<PASSWORD>"

# Login
br.submit()

# Submit the hidden form on the middle page
middle_page = br.open('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand').read()
hidden_form = BeautifulSoup(middle_page, 'html.parser').find('form', {'action': '/Account/SignIn'})
if hidden_form:
    hidden_form.submit()

# Access the contents of the end page
end_page = br.open('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand').read()
print(end_page)