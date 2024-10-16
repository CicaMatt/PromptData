import asyncio
import aiohttp  # Use aiohttp for asynchronous HTTP requests

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            url = f"https://api.com/{color}"
            task = asyncio.create_task(session.get(url)) 
            tasks.append(task)

        responses = await asyncio.gather(*tasks)  # Gather all responses concurrently

        results = []
        for response in responses:
            results.append(await response.json())  # Await the JSON parsing of each response

        return results

def function_1():
    colors = ['yellow', 'green', 'blue'] #+ ~300 other ones
    loop = asyncio.get_event_loop() 
    res = loop.run_until_complete(get_color_info(colors))
    # Process your results here