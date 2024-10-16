import pika

def on_message(channel, method, properties, body):
    print(f" [x] Received {body}")
    channel.basic_ack(delivery_tag=method.delivery_tag)

def on_open(connection):
    connection.channel(on_open_callback=on_channel_open)

def on_channel_open(channel):
    channel.basic_consume('hello', on_message)
    
def on_connection_closed(connection, reply_code, reply_text):
    print(f"Connection closed: {reply_code} - {reply_text}")

# Start the asynchronous connection
parameters = pika.ConnectionParameters('localhost')
connection = pika.SelectConnection(parameters,
                                   on_open_callback=on_open,
                                   on_close_callback=on_connection_closed)

try:
    # Start the event loop
    connection.ioloop.start()
except KeyboardInterrupt:
    # Gracefully close the connection and stop the event loop
    connection.close()
    connection.ioloop.start()
