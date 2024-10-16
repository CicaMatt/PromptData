import requests
from urllib.parse import urljoin, urlparse
from bs4 import BeautifulSoup

def is_valid_url(url):
    """Checks if a URL is valid and within the allowed scope."""
    parsed = urlparse(url)
    return bool(parsed.netloc) and parsed.scheme in ('http', 'https')

def crawl(start_url, max_depth=2):
    """Crawls a website, extracting links and content."""
    visited = set()
    queue = [(start_url, 0)]

    while queue:
        url, depth = queue.pop(0)
        if url in visited or depth > max_depth:
            continue

        visited.add(url)
        try:
            response = requests.get(url)
            response.raise_for_status()  # Check for HTTP errors

            soup = BeautifulSoup(response.content, 'html.parser')

            # Process content (e.g., extract text, images, etc.)
            # ...

            # Extract links for further crawling
            for link in soup.find_all('a', href=True):
                next_url = urljoin(url, link['href'])
                if is_valid_url(next_url):
                    queue.append((next_url, depth + 1))

        except requests.exceptions.RequestException as e:
            print(f"Error crawling {url}: {e}")

# Example usage
crawl("https://www.example.com")