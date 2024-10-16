from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup

# Set up your WebDriver (replace with your actual browser driver)
driver = webdriver.Chrome()  # or webdriver.Firefox(), etc.

driver.get('https://sso.mims.com/Account/SignIn')

# Locate and fill in the login form
email_field = driver.find_element(By.ID, 'EmailAddress') 
email_field.send_keys('<USERNAME>')

password_field = driver.find_element(By.ID, 'Password')
password_field.send_keys('<PASSWORD>')

# Submit the form
submit_button = driver.find_element(By.XPATH, '//button[text()="Sign in"]') 
submit_button.click()

# Wait for the final page to load
WebDriverWait(driver, 10).until(EC.url_contains('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand'))

# Now you should be on the end page
page_source = driver.page_source
soup = BeautifulSoup(page_source, 'html.parser')

# Extract all the links
links = [a['href'] for a in soup.find_all('a', href=True)]

# Close the browser
driver.quit()