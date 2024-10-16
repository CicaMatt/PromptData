import pika

# Connect to RabbitMQ
connection = pika.SelectConnection()
channel = connection.channel()

# Declare a queue
queue_name = 'my_queue'
channel.queue_declare(queue=queue_name)

# Send a message to the queue
message = 'Hello, world!'
channel.basic_publish(exchange='', routing_key=queue_name, body=message)

# Wait for a response from RabbitMQ using an event loop
def on_response(frame):
    print(f'Received response: {frame}')
connection.add_listener(on_response)