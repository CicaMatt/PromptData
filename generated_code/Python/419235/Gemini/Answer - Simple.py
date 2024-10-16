import requests
from bs4 import BeautifulSoup

def crawl_website(url, max_depth=2, visited_urls=None):
  if visited_urls is None:
    visited_urls = set()

  if max_depth == 0 or url in visited_urls:
    return

  visited_urls.add(url)

  try:
    response = requests.get(url)
    response.raise_for_status()  # Check for HTTP errors

    soup = BeautifulSoup(response.content, 'html.parser')

    # Extract information from the page (e.g., links, text, images)
    # ... your logic here ...

    for link in soup.find_all('a', href=True):
      next_url = link['href']
      if next_url.startswith('http'):
        crawl_website(next_url, max_depth - 1, visited_urls)

  except requests.exceptions.RequestException as e:
    print(f"Error crawling {url}: {e}")

# Start crawling from a seed URL
seed_url = "https://www.example.com"
crawl_website(seed_url)