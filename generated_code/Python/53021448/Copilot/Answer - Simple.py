import asyncio
import aiohttp

def function_1():
    colors = ['yellow', 'green', 'blue'] #+ ~300 other ones
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    res = loop.run_until_complete(get_color_info(colors))

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            print("getting color")
            url = "https://api.com/{}/".format(color)
            tasks.append(fetch(session, url))
        responses = await asyncio.gather(*tasks)
    return responses

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.json()
