import pika

connection = pika.BlockingConnection()
channel = connection.channel()
queue_name = 'my_queue'

# Declare the queue
channel.queue_declare(queue_name)

# Consume messages from the queue
while True:
    message = channel.basic_get(queue_name, no_ack=True)
    if message is None:
        # No more messages available on the queue
        break
    else:
        print('Received message:', message.body)