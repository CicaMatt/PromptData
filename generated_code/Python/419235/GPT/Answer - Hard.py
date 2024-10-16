import asyncio
import aiohttp
import re
import aiofiles
from urllib.parse import urlparse, urljoin
from bs4 import BeautifulSoup
import logging

# Setup logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger()

USER_AGENT = "CustomCrawlerBot/1.0 (+https://example.com/bot)"  # Customize this appropriately
MAX_CONCURRENT_REQUESTS = 5  # Adjust based on your needs
THROTTLE_TIME = 1  # Time between requests to the same domain, in seconds

class WebCrawler:
    def __init__(self, start_url):
        self.start_url = start_url
        self.visited_urls = set()
        self.semaphore = asyncio.Semaphore(MAX_CONCURRENT_REQUESTS)
        self.robots_cache = {}

    async def fetch(self, session, url):
        """Fetch the content of a URL while respecting the robots.txt file."""
        async with self.semaphore:
            logger.info(f"Fetching: {url}")
            async with session.get(url, headers={"User-Agent": USER_AGENT}) as response:
                if response.status == 200:
                    content_type = response.headers.get('Content-Type', '')
                    if "text/html" in content_type:
                        return await response.text()
                logger.warning(f"Non-HTML or failed response from {url}, status: {response.status}")
                return None

    async def parse_links(self, html_content, base_url):
        """Extracts all links from the HTML content."""
        soup = BeautifulSoup(html_content, 'html.parser')
        links = set()
        for anchor in soup.find_all('a', href=True):
            href = anchor['href']
            # Resolve relative URLs
            full_url = urljoin(base_url, href)
            if self.is_valid_url(full_url):
                links.add(full_url)
        return links

    def is_valid_url(self, url):
        """Check if the URL is valid and follows the robots.txt rules."""
        parsed_url = urlparse(url)
        if parsed_url.scheme not in ('http', 'https'):
            return False
        if url in self.visited_urls:
            return False
        return True

    async def get_robots_txt(self, session, base_url):
        """Fetch and parse the robots.txt file."""
        parsed_url = urlparse(base_url)
        robots_url = f"{parsed_url.scheme}://{parsed_url.netloc}/robots.txt"

        if base_url in self.robots_cache:
            return self.robots_cache[base_url]

        async with session.get(robots_url, headers={"User-Agent": USER_AGENT}) as response:
            if response.status == 200:
                content = await response.text()
                disallowed_paths = set()
                for line in content.splitlines():
                    if line.startswith("Disallow:"):
                        path = line.split(":")[1].strip()
                        disallowed_paths.add(path)
                self.robots_cache[base_url] = disallowed_paths
                return disallowed_paths
            else:
                self.robots_cache[base_url] = set()  # Assume no restrictions
                return set()

    async def crawl(self, session, url):
        """Crawl a given URL and its links, adhering to rules and avoiding already visited URLs."""
        parsed_url = urlparse(url)
        base_url = f"{parsed_url.scheme}://{parsed_url.netloc}"

        if not self.is_valid_url(url):
            return

        # Check robots.txt
        disallowed_paths = await self.get_robots_txt(session, base_url)
        for path in disallowed_paths:
            if url.startswith(f"{base_url}{path}"):
                logger.info(f"URL {url} disallowed by robots.txt")
                return

        # Fetch and parse the URL content
        html_content = await self.fetch(session, url)
        if html_content is None:
            return

        # Mark URL as visited
        self.visited_urls.add(url)

        # Parse links and recursively crawl them
        links = await self.parse_links(html_content, url)
        for link in links:
            await self.crawl(session, link)

    async def start(self):
        """Start the crawling process."""
        async with aiohttp.ClientSession() as session:
            await self.crawl(session, self.start_url)


if __name__ == "__main__":
    start_url = "https://example.com"  # Set your starting URL here
    crawler = WebCrawler(start_url)
    
    # Run the crawler
    asyncio.run(crawler.start())
