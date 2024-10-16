import scrapy
from scrapy.http import FormRequest, Request
from selenium import webdriver
from selenium.webdriver.common.by import By
import os
import logging

class MySpider(scrapy.Spider):
    name = 'my_spider'
    start_urls = ['http://my_domain.com/']

    def get_cookies(self):
        """Log in using Selenium, retrieve cookies, and return them in a format usable by Scrapy."""
        try:
            logging.info("Initializing WebDriver...")
            driver = webdriver.Firefox()
            driver.implicitly_wait(30)
            base_url = "http://www.my_domain.com/"
            driver.get(base_url)

            # Log in using the form
            driver.find_element(By.NAME, "USER").clear()
            driver.find_element(By.NAME, "USER").send_keys(os.getenv('MY_USERNAME'))
            driver.find_element(By.NAME, "PASSWORD").clear()
            driver.find_element(By.NAME, "PASSWORD").send_keys(os.getenv('MY_PASSWORD'))
            driver.find_element(By.NAME, "submit").click()

            # Extract cookies from the browser
            selenium_cookies = driver.get_cookies()
            driver.quit()

            # Format cookies for Scrapy
            scrapy_cookies = {cookie['name']: cookie['value'] for cookie in selenium_cookies}
            logging.info("Cookies successfully retrieved.")
            return scrapy_cookies
        except Exception as e:
            logging.error(f"Error during Selenium login: {e}")
            return None

    def start_requests(self):
        """Override start_requests to include cookies."""
        cookies = self.get_cookies()

        if cookies:
            logging.info("Starting request with cookies.")
            yield Request(
                url=self.start_urls[0],
                cookies=cookies,
                callback=self.login
            )
        else:
            logging.error("Failed to retrieve cookies. Aborting.")
            return

    def login(self, response):
        """Handle the login process using FormRequest."""
        logging.info("Sending login form request.")
        return FormRequest.from_response(
            response,
            formname='login_form',
            formdata={'USER': os.getenv('MY_USERNAME'), 'PASSWORD': os.getenv('MY_PASSWORD')},
            callback=self.after_login
        )

    def after_login(self, response):
        """Verify successful login by checking for a specific element."""
        if "Login failed" in response.text:
            logging.error("Login failed.")
            return

        logging.info("Login successful.")
        # Extract content or proceed with scraping
        title = response.xpath('/html/head/title/text()').get()
        logging.info(f"Page title after login: {title}")
