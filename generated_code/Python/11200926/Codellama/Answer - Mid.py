import scrapy
from selenium import webdriver
from scrapy.http import FormRequest
from scrapy.selector import Selector

class MySpider(scrapy.Spider):
    name = "myspider"
    start_urls = ["http://www.mydomain.com/"]

    def __init__(self):
        # Initialize the webdriver
        self.driver = webdriver.Firefox()

    def parse(self, response):
        # Perform the login request
        formdata = {
            'USER': 'my_username',
            'PASSWORD': 'my_password'
        }
        return FormRequest.from_response(response, formdata=formdata, callback=self.after_login)

    def after_login(self, response):
        # Check the response from the website to ensure that the login was successful
        if response.status == 200:
            # Extract the HTML content from the response
            hxs = Selector(response)
            # Use XPath to extract the desired data
            title = hxs.xpath('//title/text()').get()
            # Print the extracted data
            print(title)
        else:
            # If the login was not successful, raise an exception
            raise Exception("Login failed")