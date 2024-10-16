from scrapy import Spider
from scrapy.http import Request
from selenium import webdriver

class MySpider(Spider):
    name = 'MySpider'
    start_urls = ['http://my_domain.com/']

    def get_cookies(self, driver):
        driver = webdriver.Firefox()
        driver.implicitly_wait(30)
        base_url = "http://www.my_domain.com/"
        driver.get(base_url)
        driver.find_element_by_name("USER").clear()
        driver.find_element_by_name("USER").send_keys("my_username")
        driver.find_element_by_name("PASSWORD").clear()
        driver.find_element_by_name("PASSWORD").send_keys("my_password")
        driver.find_element_by_name("submit").click()
        cookies = driver.get_cookie()
        driver.close()
        return cookies

    def parse(self, response):
        my_cookies = self.get_cookies(response)
        return Request(url="http://my_domain.com/",
            cookies=my_cookies,
            callback=self.login)

    def login(self, response):
        hxs = response.selector
        