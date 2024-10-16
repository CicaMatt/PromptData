import os
from scrapy.spiders import Spider
from scrapy.http import FormRequest, Request
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options
from scrapy.utils.project import get_project_settings
import logging

class MySpider(Spider):
    name = 'my_spider'
    start_urls = ['http://my_domain.com/']

    # Ensure credentials are retrieved securely, either via environment variables or configuration
    USERNAME = os.getenv('MY_USERNAME', 'default_user')
    PASSWORD = os.getenv('MY_PASSWORD', 'default_pass')

    def get_cookies(self):
        """
        Uses Selenium to log into the website and retrieve cookies for session management.
        """
        # Configure headless mode for Selenium (optional)
        options = Options()
        options.headless = True  # Set to False for debugging in a browser window

        driver = webdriver.Firefox(options=options)
        driver.implicitly_wait(10)
        base_url = "http://www.my_domain.com/"

        try:
            driver.get(base_url)

            # Use WebDriverWait and expected conditions for secure and reliable interaction
            driver.find_element(By.NAME, "USER").clear()
            driver.find_element(By.NAME, "USER").send_keys(self.USERNAME)
            driver.find_element(By.NAME, "PASSWORD").clear()
            driver.find_element(By.NAME, "PASSWORD").send_keys(self.PASSWORD)
            driver.find_element(By.NAME, "submit").click()

            # Check if login was successful by searching for a post-login element
            # Adjust the XPATH or CSS selectors as per your target page structure
            if "Dashboard" in driver.title:
                logging.info("Successfully logged in with Selenium.")
            else:
                logging.error("Login failed. Please check your credentials or login flow.")

            cookies = driver.get_cookies()
        finally:
            driver.quit()

        return cookies

    def start_requests(self):
        """
        Initiates the Scrapy requests, transferring cookies obtained from Selenium.
        """
        cookies = self.get_cookies()
        logging.info("Transferring cookies from Selenium to Scrapy.")

        # Start the Scrapy requests, with the Selenium-obtained cookies
        for url in self.start_urls:
            yield Request(url=url, cookies={cookie['name']: cookie['value'] for cookie in cookies}, callback=self.parse)

    def parse(self, response):
        """
        Handles the response after the initial request, including cookie-based session management.
        """
        # Check if logged in by looking for certain elements in the page content (adjust as needed)
        if "Logout" in response.text:
            logging.info("Successfully logged in with Scrapy session.")
            return self.after_login(response)
        else:
            logging.error("Login failed in Scrapy session.")
            return

    def after_login(self, response):
        """
        Processes the page after logging in, can extract data or follow links.
        """
        # Example: Extracting the title of the page
        title = response.xpath('//title/text()').get()
        logging.info(f"Page title after login: {title}")

        # Add more scraping logic here depending on what data you need to extract or interact with.
        # e.g., extracting more data, following additional links, etc.
        # self.extract_data(response)
