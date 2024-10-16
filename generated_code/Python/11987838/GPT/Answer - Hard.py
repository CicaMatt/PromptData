import pika

def on_message(channel, method_frame, header_frame, body):
    print(f"Received message: {body}")
    channel.basic_ack(delivery_tag=method_frame.delivery_tag)

# Setup connection using BlockingConnection
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# Declare the queue
channel.queue_declare(queue='my_queue')

# Subscribe to the queue
channel.basic_consume(queue='my_queue', on_message_callback=on_message)

print('Waiting for messages...')
channel.start_consuming()

def on_message(channel, method_frame, header_frame, body):
    print(f"Received message: {body}")
    channel.basic_ack(delivery_tag=method_frame.delivery_tag)

def on_open(connection):
    connection.channel(on_open_callback=on_channel_open)

def on_channel_open(channel):
    channel.basic_consume(queue='my_queue', on_message_callback=on_message)

# Setup connection using SelectConnection
params = pika.ConnectionParameters('localhost')

connection = pika.SelectConnection(params, on_open_callback=on_open)

try:
    print('Waiting for messages asynchronously...')
    connection.ioloop.start()
except KeyboardInterrupt:
    connection.close()
    connection.ioloop.start()
