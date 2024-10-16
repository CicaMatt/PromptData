import scrapy
from scrapy.http import FormRequest, Request
from selenium import webdriver

class MySpider(scrapy.Spider):
    name = 'MySpider'
    start_urls = ['http://my_domain.com/']

    def get_cookies(self):
        driver = webdriver.Firefox()
        driver.implicitly_wait(30)
        base_url = "http://www.my_domain.com/"
        driver.get(base_url)

        # Log into the website using Selenium
        driver.find_element_by_name("USER").clear()
        driver.find_element_by_name("USER").send_keys("my_username")
        driver.find_element_by_name("PASSWORD").clear()
        driver.find_element_by_name("PASSWORD").send_keys("my_password")
        driver.find_element_by_name("submit").click()

        # Extract cookies from the Selenium driver
        cookies = driver.get_cookies()
        driver.quit()  # Close the Selenium driver after getting the cookies
        return cookies

    def start_requests(self):
        # Get cookies using Selenium
        cookies = self.get_cookies()

        # Convert Selenium cookies to Scrapy cookies format
        scrapy_cookies = {cookie['name']: cookie['value'] for cookie in cookies}

        # Start the request with cookies and call the login callback
        yield Request(
            url=self.start_urls[0],
            cookies=scrapy_cookies,
            callback=self.login
        )

    def login(self, response):
        # Log information to see if we have logged in successfully
        self.logger.info(f"Login page title: {response.xpath('//title/text()').get()}")
        
        # Assuming there's a form to log in again (or just for example purposes)
        return FormRequest.from_response(
            response,
            formname='login_form',
            formdata={'USER': 'my_username', 'PASSWORD': 'my_password'},
            callback=self.after_login
        )

    def after_login(self, response):
        # Print out the title of the page after login to see if we succeeded
        self.logger.info(f"After login page title: {response.xpath('//title/text()').get()}")

        # Continue scraping or perform other actions
