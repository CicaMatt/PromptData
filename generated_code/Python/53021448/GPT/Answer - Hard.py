import aiohttp
import asyncio
import logging

# Set up logging for error tracking
logging.basicConfig(level=logging.INFO)

async def fetch_color(session, color, semaphore):
    """
    Fetch color information from the API asynchronously.
    Uses a semaphore to limit the number of concurrent requests.
    """
    url = f"https://api.com/{color}/"
    async with semaphore:
        try:
            async with session.get(url, timeout=10) as response:
                logging.info(f"Getting color: {color}")
                return await response.json()
        except Exception as e:
            logging.error(f"Error fetching {color}: {e}")
            return None

async def get_color_info(colors, max_concurrent_requests=10):
    """
    Get color information for all colors concurrently with a limit on concurrent requests.
    """
    semaphore = asyncio.Semaphore(max_concurrent_requests)
    async with aiohttp.ClientSession() as session:
        tasks = [fetch_color(session, color, semaphore) for color in colors]
        responses = await asyncio.gather(*tasks)
    return responses

def function_1():
    """
    Entry point that triggers the asynchronous color fetching.
    Creates an event loop, runs it until the tasks are completed.
    """
    colors = ['yellow', 'green', 'blue', ...]  # Add your full list of colors here
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    try:
        results = loop.run_until_complete(get_color_info(colors))
        logging.info("All colors fetched.")
        return results
    finally:
        loop.close()

if __name__ == "__main__":
    function_1()
