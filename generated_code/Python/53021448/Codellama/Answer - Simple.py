import asyncio
import requests

async def get_color_info(colors):
    responses = []
    for color in colors:
        print("Getting color {}".format(color))
        url = "https://api.com/{}/".format(color)
        data = await asyncio.get_event_loop().run_in_executor(None, requests.get, url)
        responses.append(data.json())
    return responses

async def function_1():
    colors = ['yellow', 'green', 'blue'] # + ~300 other ones
    responses = await get_color_info(colors)
    for response in responses:
        print(response)

if __name__ == "__main__":
    loop = asyncio.get_event_loop()
    loop.run_until_complete(function_1())