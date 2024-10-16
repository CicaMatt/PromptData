import asyncio

async def get_color_info(colors):
    loop = asyncio.get_event_loop()
    responses = await asyncio.gather(*[loop.create_task(requests.get('https://api.com/{}/'.format(color))) for color in colors])
    return responses

async def run(colors):
    # Run the coroutine
    responses = await asyncio.run(get_color_info(['yellow', 'green', 'blue']))