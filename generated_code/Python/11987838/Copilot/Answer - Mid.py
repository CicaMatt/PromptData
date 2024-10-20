import pika
import pika.adapters.select_connection

def blocking_connection_example():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='hello')

    channel.basic_publish(exchange='',
                          routing_key='hello',
                          body='Hello World!')
    print(" [x] Sent 'Hello World!'")
    connection.close()

blocking_connection_example()

def on_open(connection):
    connection.channel(on_channel_open)

def on_channel_open(channel):
    channel.queue_declare(queue='hello', callback=on_queue_declared)

def on_queue_declared(frame):
    channel.basic_publish(exchange='',
                          routing_key='hello',
                          body='Hello World!')
    print(" [x] Sent 'Hello World!'")
    connection.ioloop.stop()

parameters = pika.ConnectionParameters('localhost')
connection = pika.SelectConnection(parameters, on_open)
connection.ioloop.start()
