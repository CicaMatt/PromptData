import asyncio
import aiohttp

async def get_color_info(colors):
    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            url = f"https://api.com/{color}"
            task = asyncio.create_task(session.get(url))
            tasks.append(task)

        responses = await asyncio.gather(*tasks)
        return [await response.json() for response in responses]

async def main():
    colors = ['yellow', 'green', 'blue'] + ['other_color' for _ in range(297)] 
    res = await get_color_info(colors)
    print(res)

if __name__ == "__main__":
    asyncio.run(main())