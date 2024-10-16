import asyncio
import requests

async def get_color_info(colors):
    loop = asyncio.get_event_loop()
    responses = []
    for color in colors:
        print("Getting color {}...".format(color))
        url = "https://api.com/{}/".format(color)
        data = await loop.create_task(requests.get(url))
        r = await data.json()
        responses.append(r)
    return responses

colors = ['yellow', 'green', 'blue'] # ... ~300 other ones]
loop = asyncio.new_event_loop()
asyncio.set_event_loop(loop)
res = loop.run_until_complete(get_color_info(colors))