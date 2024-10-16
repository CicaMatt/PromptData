import pika

def on_message_callback(ch, method, properties, body):
    print(f"Received message: {body}")
    ch.basic_ack(delivery_tag=method.delivery_tag)

def main():
    connection_params = pika.ConnectionParameters('localhost')  # Adjust as needed
    connection = pika.SelectConnection(connection_params)

    channel = connection.channel()
    channel.queue_declare(queue='my_queue')
    channel.basic_consume(queue='my_queue', on_message_callback=on_message_callback)

    try:
        while connection.is_open:
            connection.process_data_events()  # Non-blocking event handling
    finally:
        connection.close()

if __name__ == '__main__':
    main()