import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse

def crawl_website(start_url, max_depth=2):
    """
    Crawls a website starting from the given URL and extracts links up to the specified depth.

    Args:
        start_url: The URL to start crawling from.
        max_depth: The maximum depth to crawl (default 2).

    Returns:
        A set of unique URLs found during the crawl.
    """

    visited_urls = set()
    urls_to_visit = [(start_url, 0)]  # (url, current_depth)

    while urls_to_visit:
        url, current_depth = urls_to_visit.pop(0)

        if url in visited_urls or current_depth > max_depth:
            continue

        visited_urls.add(url)

        try:
            response = requests.get(url)
            response.raise_for_status()  # Raise an exception for bad status codes

            soup = BeautifulSoup(response.content, 'html.parser')

            for link in soup.find_all('a'):
                href = link.get('href')
                if href:
                    absolute_url = urljoin(url, href)
                    if is_same_domain(start_url, absolute_url):
                        urls_to_visit.append((absolute_url, current_depth + 1))

        except requests.exceptions.RequestException as e:
            print(f"Error crawling {url}: {e}")

    return visited_urls

def is_same_domain(url1, url2):
    """
    Checks if two URLs belong to the same domain.

    Args:
        url1: The first URL.
        url2: The second URL.

    Returns:
        True if the URLs belong to the same domain, False otherwise.
    """

    parsed_url1 = urlparse(url1)
    parsed_url2 = urlparse(url2)
    return parsed_url1.netloc == parsed_url2.netloc

# Example usage
start_url = "https://www.example.com"  # Replace with your desired URL
crawled_urls = crawl_website(start_url)

for url in crawled_urls:
    print(url)