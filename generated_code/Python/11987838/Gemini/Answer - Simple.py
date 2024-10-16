import pika

# BlockingConnection
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# This operation blocks the execution until it completes
channel.queue_declare(queue='hello')

# SelectConnection
connection = pika.SelectConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# This operation is non-blocking
channel.queue_declare(queue='hello', callback=on_queue_declared)

# You need an event loop to handle events from the connection
connection.ioloop.start()