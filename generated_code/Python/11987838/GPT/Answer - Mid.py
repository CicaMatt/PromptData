import pika

# Example using BlockingConnection (synchronous)
def blocking_connection_example():
    try:
        # This will block until the connection is established
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        channel = connection.channel()

        # Declare a queue (if it doesn't already exist)
        channel.queue_declare(queue='test_queue')

        # Publish a message
        channel.basic_publish(exchange='', routing_key='test_queue', body='Hello, World!')

        print("Message sent using BlockingConnection")

        # Close the connection
        connection.close()
    except pika.exceptions.AMQPConnectionError as e:
        print(f"Error occurred with BlockingConnection: {e}")


# Example using SelectConnection (asynchronous)
class SelectConnectionExample:
    def __init__(self):
        self.connection = None
        self.channel = None

    def on_open(self, connection):
        """Callback when the connection is open."""
        self.connection = connection
        self.connection.channel(on_open_callback=self.on_channel_open)

    def on_channel_open(self, channel):
        """Callback when the channel is open."""
        self.channel = channel
        self.channel.queue_declare(queue='test_queue', callback=self.on_queue_declared)

    def on_queue_declared(self, method_frame):
        """Callback when the queue is declared."""
        self.channel.basic_publish(exchange='', routing_key='test_queue', body='Hello, World!')
        print("Message sent using SelectConnection")

        # Close the connection
        self.connection.close()

    def on_connection_closed(self, connection, reason):
        """Callback when the connection is closed."""
        print(f"Connection closed: {reason}")

    def run(self):
        parameters = pika.ConnectionParameters('localhost')

        # Set up a non-blocking connection using SelectConnection
        self.connection = pika.SelectConnection(
            parameters, on_open_callback=self.on_open, on_close_callback=self.on_connection_closed
        )

        # Start the event loop
        try:
            self.connection.ioloop.start()
        except KeyboardInterrupt:
            self.connection.close()
            self.connection.ioloop.start()


if __name__ == "__main__":
    # Example 1: Using BlockingConnection (synchronous)
    blocking_connection_example()

    # Example 2: Using SelectConnection (asynchronous)
    async_example = SelectConnectionExample()
    async_example.run()
