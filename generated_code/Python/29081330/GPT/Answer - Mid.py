import scrapy
from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor
from tutorial.items import DmozItem

class TsrSpider(CrawlSpider):
    name = 'tsr'
    allowed_domains = ['thestudentroom.co.uk']
    start_urls = ['http://www.thestudentroom.co.uk/forumdisplay.php?f=143']

    custom_settings = {
        'DOWNLOAD_DELAY': 4,
        'USER_AGENT': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:35.0) Gecko/20100101 Firefox/35.0',
    }

    rules = (
        Rule(
            LinkExtractor(
                allow=('forumdisplay\.php\?f=143&page=\d+',),
                restrict_xpaths="//li[@class='page']"
            ),
            follow=True
        ),
        Rule(
            LinkExtractor(
                allow=('showthread\.php\?t=\d+&page=\d+',),
                restrict_xpaths="//li[@class='page']"
            ),
            callback='parse_link',
            follow=True
        ),
        Rule(
            LinkExtractor(
                allow=('showthread\.php\?t=\d+',),
                restrict_xpaths="//tr[contains(@class, 'thread')]"
            ),
            callback='parse_link',
            follow=True
        ),
    )

    def parse_link(self, response):
        # Itera sui post
        for post in response.xpath("//li[contains(@class, 'post')]"):
            rating = post.xpath(".//span[@class='score']/text()").get(default='0')
            item = DmozItem()
            item['post'] = post.xpath(".//blockquote[@class='postcontent restore']/text()").getall()
            item['link'] = response.url
            item['topic'] = response.xpath("//div[@class='forum-header section-header']/h1/span/text()").get()
            item['rating'] = rating
            yield item
