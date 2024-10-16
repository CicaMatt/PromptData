import scrapy

class MySpider(scrapy.Spider):
    name = 'my_spider'
    start_urls = ['https://example.com']  # Replace with your target URL

    def parse(self, response):
        # Example: Extracting all links on the page
        for link in response.css('a::attr(href)').getall():
            yield {'Link': link}

        # Example: Follow each link recursively
        for next_page in response.css('a::attr(href)').getall():
            if next_page is not None:
                yield response.follow(next_page, self.parse)
