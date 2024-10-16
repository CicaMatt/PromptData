import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
from collections import deque

# Simple Web Crawler
class SimpleCrawler:
    def __init__(self, start_url, max_pages=50):
        self.start_url = start_url
        self.visited_urls = set()
        self.queue = deque([start_url])
        self.max_pages = max_pages
    
    def crawl(self):
        while self.queue and len(self.visited_urls) < self.max_pages:
            url = self.queue.popleft()
            if url in self.visited_urls:
                continue

            self.visited_urls.add(url)
            print(f"Crawling: {url}")
            
            try:
                response = requests.get(url, timeout=5)
                if response.status_code != 200:
                    continue

                soup = BeautifulSoup(response.text, 'html.parser')
                for link in soup.find_all('a', href=True):
                    full_url = urljoin(url, link['href'])
                    if self.is_valid_url(full_url):
                        self.queue.append(full_url)

            except requests.RequestException as e:
                print(f"Failed to retrieve {url}: {e}")

    def is_valid_url(self, url):
        parsed = urlparse(url)
        return bool(parsed.netloc) and bool(parsed.scheme)

# Example usage
if __name__ == "__main__":
    start_url = "https://example.com"
    crawler = SimpleCrawler(start_url, max_pages=20)
    crawler.crawl()
