import asyncio
import aiohttp  # Use aiohttp for asynchronous HTTP requests

async def get_color_info(session, color):  # Pass the aiohttp session
    url = f"https://api.com/{color}" 
    try:
        async with session.get(url) as response:
            if response.status == 200:
                return await response.json()
            else:
                print(f"Error fetching data for {color}: {response.status}")
    except aiohttp.ClientError as e:
        print(f"Error fetching data for {color}: {e}")

async def main():
    colors = ['yellow', 'green', 'blue'] + ['other_color' for _ in range(297)] 

    async with aiohttp.ClientSession() as session:
        tasks = []
        for color in colors:
            task = asyncio.create_task(get_color_info(session, color))
            tasks.append(task)

        responses = await asyncio.gather(*tasks)  # Gather results concurrently

    # Process the responses (filter out None values if there were errors)
    valid_responses = [r for r in responses if r is not None]
    # ... your logic here ...

if __name__ == "__main__":
    asyncio.run(main())