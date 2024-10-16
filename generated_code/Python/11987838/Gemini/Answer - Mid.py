import pika
import asyncio

async def main():
    connection = pika.SelectConnection(
        pika.ConnectionParameters('localhost'),
        on_open_callback=on_connection_open,
    )

    try:
        # Start the IOLoop (asyncio event loop in this case)
        await connection.ioloop.start()
    finally:
        # Gracefully close the connection
        await connection.close()

async def on_connection_open(connection):
    channel = await connection.channel()
    # ... (Set up your queues, exchanges, and message handling here)

if __name__ == "__main__":
    asyncio.run(main())