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
        driver.find_element_by_name("USER").clear()
        driver.find_element_by_name("USER").send_keys("my_username")
        driver.find_element_by_name("PASSWORD").clear()
        driver.find_element_by_name("PASSWORD").send_keys("my_password")
        driver.find_element_by_name("submit").click()
        cookies = driver.get_cookies()
        driver.close()
        return cookies

    def start_requests(self):
        cookies = self.get_cookies()
        for url in self.start_urls:
            yield Request(url=url, cookies={cookie['name']: cookie['value'] for cookie in cookies}, callback=self.parse)

    def parse(self, response):
        return FormRequest.from_response(response,
            formname='login_form',
            formdata={'USER': 'my_username', 'PASSWORD': 'my_password'},
            callback=self.after_login)

    def after_login(self, response):
        if "authentication failed" in response.body.decode('utf-8'):
            self.logger.error("Login failed")
            return
        else:
            self.logger.info("Login successful")
            # Continue scraping after successful login
            return scrapy.Request(url="http://my_domain.com/some_page", callback=self.parse_page)

    def parse_page(self, response):
        # Your parsing code here
        self.logger.info("Parsing page")
        print(response.xpath('/html/head/title/text()').get())
