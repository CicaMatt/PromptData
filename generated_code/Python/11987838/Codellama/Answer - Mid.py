import pika

# Establish connection to RabbitMQ server
connection = pika.BlockingConnection(pika.ConnectionParameters('rabbitmq://localhost'))
channel = connection.channel()

# Declare queue for messages
queue_name = 'my_queue'
channel.queue_declare(queue=queue_name)

# Consume messages from queue
def callback(ch, method, properties, body):
    print("Received message:", body)

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

# Establish connection to RabbitMQ server
connection = pika.SelectConnection(pika.ConnectionParameters('rabbitmq://localhost'))
channel = connection.channel()

# Declare queue for messages
queue_name = 'my_queue'
channel.queue_declare(queue=queue_name)

# Consume messages from queue using select() method
def callback(ch, method, properties, body):
    print("Received message:", body)

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)