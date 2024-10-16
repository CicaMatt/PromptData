import pika
import threading

# BlockingConnection Example
def blocking_connection_example():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()
    channel.queue_declare(queue='hello')
    channel.basic_publish(exchange='', routing_key='hello', body='Hello World!')
    print(" [x] Sent 'Hello World!'")
    connection.close()

# SelectConnection Example
def on_open(connection):
    connection.channel(on_channel_open)

def on_channel_open(channel):
    channel.queue_declare(queue='hello', callback=on_queue_declared)

def on_queue_declared(frame):
    channel.basic_publish(exchange='', routing_key='hello', body='Hello World!')
    print(" [x] Sent 'Hello World!'")
    connection.close()

def select_connection_example():
    parameters = pika.ConnectionParameters('localhost')
    connection = pika.SelectConnection(parameters, on_open_callback=on_open)
    try:
        connection.ioloop.start()
    except KeyboardInterrupt:
        connection.close()
        connection.ioloop.start()

# Running examples in separate threads
threading.Thread(target=blocking_connection_example).start()
threading.Thread(target=select_connection_example).start()
