from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup

# Replace with your actual username and password
USERNAME = 'your_username'
PASSWORD = 'your_password'

# Set up the WebDriver (make sure you have the appropriate driver installed)
driver = webdriver.Chrome()  # Or use Firefox, Edge, etc.

# Navigate to the login page
driver.get('https://sso.mims.com/Account/SignIn')

# Locate and fill in the login credentials
email_field = driver.find_element(By.ID, 'EmailAddress')
password_field = driver.find_element(By.ID, 'Password')
email_field.send_keys(USERNAME)
password_field.send_keys(PASSWORD)

# Submit the login form
submit_button = driver.find_element(By.XPATH, '//button[text()="Sign In"]')
submit_button.click()

# Wait for the redirect to complete
WebDriverWait(driver, 10).until(
    EC.url_to_be('https://mims.com/india/browse/alphabet/a?cat=drug&tab=brand')
)

# Now you are on the target page, extract the links
page_source = driver.page_source
soup = BeautifulSoup(page_source, 'html.parser')

# Find all the links on the page
all_links = [link['href'] for link in soup.find_all('a', href=True)]

# Filter links based on your needs (if required)

# Close the browser
driver.quit()

# Print or process the collected links
print(all_links)