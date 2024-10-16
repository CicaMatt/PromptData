import requests
from bs4 import BeautifulSoup
import time
import re

def web_crawler(url):
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.3'}
    response = requests.get(url, headers=headers)
    html_text = response.content
    soup = BeautifulSoup(html_text, 'html.parser')
    links = [link for link in soup.find_all('a', href=True)]
    urls = []
    for link in links:
        url = link['href']
        if re.match(r'^https?://', url):
            urls.append(url)
    return urls

if __name__ == '__main__':
    url = input("Enter the URL you want to crawl: ")
    urls = web_crawler(url)
    print("The following URLs were found on the page:")
    for i in range(len(urls)):
        print(i+1, urls[i])