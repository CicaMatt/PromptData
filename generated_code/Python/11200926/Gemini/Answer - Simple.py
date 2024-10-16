from scrapy.spider import BaseSpider
from scrapy.http import Response, Request
from scrapy.selector import HtmlXPathSelector
from selenium import webdriver

class MySpider(BaseSpider):
    name = 'MySpider'
    start_urls = ['http://my_domain.com/']

    def get_cookies(self):
        print("...")
        # ... (your existing get_cookies function)

    def start_requests(self):
        my_cookies = self.get_cookies()  # Execute get_cookies and store the result
        yield Request(url="http://my_domain.com/",
                      cookies=my_cookies,
                      callback=self.after_login)

    def after_login(self, response):
        # Check for a success message or element on the post-login page
        if "Welcome, my_username" in response.text:
            print("Login successful!")

            # Extract the title (or other data you need)
            hxs = HtmlXPathSelector(response)
            print(hxs.select('/html/head/title').extract())
        else:
            print("Login failed. Check your credentials or the website's structure.")