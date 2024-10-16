import scrapy

class MySpider(scrapy.Spider):
    name = 'myspider'
    start_urls = ['http://example.com']

    def parse(self, response):
        for title in response.css('title::text'):
            yield {'title': title.get()}

        for next_page in response.css('a::attr(href)'):
            yield response.follow(next_page.get(), self.parse)

# To run the spider, save this script as myspider.py and run:
# scrapy runspider myspider.py
