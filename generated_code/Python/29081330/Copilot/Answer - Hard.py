import scrapy
from tutorial.items import DmozItem
from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor

class TsrSpider(CrawlSpider):
    name = 'tsr'
    allowed_domains = ['thestudentroom.co.uk']
    start_urls = ['http://www.thestudentroom.co.uk/forumdisplay.php?f=143']

    download_delay = 4
    user_agent = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:35.0) Gecko/20100101 Firefox/35.0'

    rules = (
        Rule(
            LinkExtractor(
                allow=('forumdisplay\.php\?f=143\&page=\d',),
                restrict_xpaths=("//li[@class='pager-page_numbers']/a"))),

        Rule(
            LinkExtractor(
                allow=('showthread\.php\?t=\d+\&page=\d+',),
                restrict_xpaths=("//li[@class='pager-page_numbers']/a")), 
            callback='parse_link'),

        Rule(
            LinkExtractor(
                allow=('showthread\.php\?t=\d+',),
                restrict_xpaths=("//tr[@class='thread  unread    ']")),
            callback='parse_link'),
        )

    def parse_link(self, response):
        # Iterate over posts.     
        for sel in response.xpath("//li[@class='post threadpost old   ']"):
            rating = sel.xpath(
                "div[@class='post-footer']//span[@class='score']/text()").extract_first(default=0)
            item = DmozItem()
            item['post'] = sel.xpath(
                "div[@class='post-content']/blockquote[@class='postcontent restore']/text()").extract()
            item['link'] = response.url
            item['topic'] = response.xpath(
                "//div[@class='forum-header section-header']/h1/span/text()").extract()
            item['rating'] = rating
            yield item
