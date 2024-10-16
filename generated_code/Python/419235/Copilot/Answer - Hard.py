import scrapy

class MySpider(scrapy.Spider):
    name = 'myspider'
    start_urls = ['https://example.com']

    def parse(self, response):
        for title in response.css('h1::text').getall():
            yield {'title': title}

        for next_page in response.css('a.next::attr(href)').getall():
            yield response.follow(next_page, self.parse)

# To run the spider, save this script as myspider.py and run the following command in your terminal:
# scrapy runspider myspider.py
