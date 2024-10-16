from scrapy.spiders import Spider
from scrapy.http import Request
from scrapy.selector import Selector
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class MySpider(Spider):
    name = 'MySpider'
    start_urls = ['http://my_domain.com/']

    def __init__(self):
        self.driver = webdriver.Firefox()  # Initialize WebDriver in the constructor

    def start_requests(self):
        yield Request(url=self.start_urls[0], callback=self.selenium_login)

    def selenium_login(self, response):
        self.driver.get(response.url)

        # Explicitly wait for elements to be present and interactable
        wait = WebDriverWait(self.driver, 10)
        user_field = wait.until(EC.presence_of_element_located((By.NAME, "USER")))
        password_field = wait.until(EC.presence_of_element_located((By.NAME, "PASSWORD")))
        submit_button = wait.until(EC.element_to_be_clickable((By.NAME, "submit")))

        user_field.send_keys("my_username")
        password_field.send_keys("my_password")
        submit_button.click()

        # Wait for the login to complete (adjust as needed)
        wait.until(EC.url_changes(response.url))

        # Extract cookies from Selenium and attach to Scrapy request
        cookies = self.driver.get_cookies()
        yield Request(url="http://my_domain.com/", cookies=cookies, callback=self.after_login)

    def after_login(self, response):
        sel = Selector(response)
        print(sel.xpath('/html/head/title/text()').get())

        # Close the WebDriver when done
        self.driver.quit()