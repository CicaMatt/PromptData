from scrapy.spider import Spider  
from scrapy.selector import Selector, Request  
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait  
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException

class MySpider(Spider):
    name = 'MySpider'
    allowed_domains = ['my_domain.com']  
    start_urls = ['http://my_domain.com/']

    def get_cookies(self):
        options = webdriver.FirefoxOptions()
        options.add_argument("--headless")  
        
        try:
            driver = webdriver.Firefox(options=options)
            driver.implicitly_wait(10)  
            driver.get("http://www.my_domain.com/")

            # Use explicit waits for better reliability
            WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.NAME, "USER"))
            )
            user_field = driver.find_element(By.NAME, "USER")
            user_field.clear()
            user_field.send_keys("my_username")

            WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.NAME, "PASSWORD"))
            )
            password_field = driver.find_element(By.NAME, "PASSWORD")
            password_field.clear()
            password_field.send_keys("my_password")

            WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable((By.NAME, "submit"))
            )
            driver.find_element(By.NAME, "submit").click()

            # Wait for the login to complete (you might need to adjust this)
            try:
                WebDriverWait(driver, 10).until(
                    EC.title_contains("Dashboard")  
                )
            except TimeoutException:
                self.logger.warning("Login might have failed. Check the page title.")

            cookies = driver.get_cookies()
        finally:
            driver.quit()  

        return cookies

    def start_requests(self):
        my_cookies = self.get_cookies()
        yield Request(
            url="http://my_domain.com/",
            cookies=my_cookies,
            callback=self.after_login
        )

    def after_login(self, response):
        # Check for successful login here (e.g., by looking for specific elements)
        if "Logout" in response.text:  
            selector = Selector(response)
            title = selector.xpath('/html/head/title/text()').get()
            print(title)
        else:
            self.logger.error("Login failed. Check your credentials or the website structure.")