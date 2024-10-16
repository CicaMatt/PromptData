import asyncio
import aiohttp

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            print(f"getting color {color}")
            url = f"https://api.com/{color}/"
            tasks.append(fetch_data(session, url))
        
        # Run all tasks concurrently
        responses = await asyncio.gather(*tasks)
    return responses

async def fetch_data(session, url):
    async with session.get(url) as response:
        return await response.json()

def function_1():
    colors = ['yellow', 'green', 'blue', ...]  # Include ~300 colors here
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    res = loop.run_until_complete(get_color_info(colors))
    return res

if __name__ == "__main__":
    result = function_1()
    print(result)
