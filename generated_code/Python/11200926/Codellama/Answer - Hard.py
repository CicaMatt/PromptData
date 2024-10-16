import scrapy
from scrapy.http import FormRequest, Request
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class MySpider(scrapy.Spider):
    name = 'MySpider'
    start_urls = ['http://my_domain.com/']

    def get_cookies(self):
        driver = webdriver.Firefox()
        driver.implicitly_wait(30)
        base_url = "http://www.my_domain.com/"
        driver.get(base_url)
        
        # Use WebDriverWait to ensure elements are present before interacting
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "USER"))).clear()
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "USER"))).send_keys("my_username")
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "PASSWORD"))).clear()
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "PASSWORD"))).send_keys("my_password")
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.NAME, "submit"))).click()
        
        cookies = driver.get_cookies()
        driver.quit()
        return cookies

    def start_requests(self):
        cookies = self.get_cookies()
        yield Request(url="http://my_domain.com/",
                      cookies={cookie['name']: cookie['value'] for cookie in cookies},
                      callback=self.login)

    def login(self, response):
        return FormRequest.from_response(response,
                                         formname='login_form',
                                         formdata={'USER': 'my_username', 'PASSWORD': 'my_password'},
                                         callback=self.after_login)

    def after_login(self, response):
        # Use scrapy's built-in selectors instead of HtmlXPathSelector
        title = response.xpath('/html/head/title/text()').get()
        self.log(f"Page title after login: {title}")
