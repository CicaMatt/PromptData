import mechanize
from bs4 import BeautifulSoup

# Log in to the website
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

# Follow the redirect to the end page
end_page = br.follow_link(text='You will be redirected to your destination shortly.')

print(BeautifulSoup(end_page, 'html.parser'))