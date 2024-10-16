import requests
from bs4 import BeautifulSoup

def crawl(url, max_pages=10):
    pages_visited = set()
    links = []
    
    while len(pages_visited) < max_pages:
        response = requests.get(url)
        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')
            for link in soup.find_all('a'):
                href = link.get('href')
                if href and href not in pages_visited:
                    links.append(href)
                    pages_visited.add(href)
        else:
            break
    
    return links