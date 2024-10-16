import scrapy

class MySpider(scrapy.Spider):
    name = 'myspider'
    start_urls = ['http://example.com']

    def parse(self, response):
        for title in response.css('h1::text').getall():
            yield {'title': title}

        for next_page in response.css('a::attr(href)').getall():
            if next_page is not None:
                yield response.follow(next_page, self.parse)

# To run the spider, save this script as myspider.py and run:
# scrapy runspider myspider.py
