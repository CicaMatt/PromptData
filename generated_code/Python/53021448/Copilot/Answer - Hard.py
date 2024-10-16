import asyncio
import aiohttp

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            print("getting color")
            url = f"https://api.com/{color}/"
            tasks.append(fetch(session, url))
        responses = await asyncio.gather(*tasks)
    return responses

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.json()

def function_1():
    colors = ['yellow', 'green', 'blue'] + ['color' + str(i) for i in range(300)]
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    res = loop.run_until_complete(get_color_info(colors))
    print(res)

if __name__ == "__main__":
    function_1()
