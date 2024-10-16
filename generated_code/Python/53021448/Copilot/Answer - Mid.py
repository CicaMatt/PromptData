import asyncio
import aiohttp

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            url = f"https://api.com/{color}/"
            tasks.append(fetch(session, url))
        responses = await asyncio.gather(*tasks)
    return responses

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.json()

def function_1():
    colors = ['yellow', 'green', 'blue', 'red', 'purple', 'orange', 'pink', 'brown', 'black', 'white']  # Add ~300 colors
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    res = loop.run_until_complete(get_color_info(colors))
    print(res)

function_1()
