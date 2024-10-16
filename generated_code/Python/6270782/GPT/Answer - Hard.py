import smtplib
import os
import logging
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

# Setup basic logging
logging.basicConfig(level=logging.INFO)

def send_mail(from_addr, to_addrs, subject, body, smtp_server, smtp_port=587, use_tls=True):
    """
    Sends an email using the SMTP protocol.

    Parameters:
        from_addr (str): The sender's email address.
        to_addrs (list): A list of recipient email addresses.
        subject (str): The email subject.
        body (str): The body of the email.
        smtp_server (str): The SMTP server address.
        smtp_port (int): The SMTP server port (default is 587).
        use_tls (bool): Whether to use TLS (default is True).

    Raises:
        Exception: Raises an exception if the email fails to send.
    """

    # Ensure that `to_addrs` is a list
    if not isinstance(to_addrs, list):
        raise ValueError("The 'to_addrs' parameter must be a list")

    # Create the message
    msg = MIMEMultipart()
    msg['From'] = from_addr
    msg['To'] = ', '.join(to_addrs)
    msg['Subject'] = subject
    msg.attach(MIMEText(body, 'plain'))

    # Try to send the email with proper error handling
    server = None
    try:
        logging.info("Connecting to SMTP server...")

        # Create the SMTP connection
        server = smtplib.SMTP(smtp_server, smtp_port)
        server.ehlo()

        if use_tls:
            logging.info("Starting TLS encryption...")
            server.starttls()  # Secure the connection

        logging.info("Sending email...")
        # Send the email
        server.sendmail(from_addr, to_addrs, msg.as_string())

        logging.info("Email sent successfully.")
    
    except smtplib.SMTPException as e:
        logging.error(f"Failed to send email: {e}")
        raise e  # Re-raise the exception for external handling if needed
    finally:
        # Always ensure the connection is closed
        if server is not None:
            logging.info("Closing the SMTP connection...")
            server.quit()

# Example usage (Make sure to set the actual SMTP server and credentials in environment variables)
if __name__ == "__main__":
    try:
        # Replace these with your own environment variables or set directly for testing
        SMTP_SERVER = os.getenv('SMTP_SERVER', 'smtp.myserver.com')
        SMTP_PORT = int(os.getenv('SMTP_PORT', 587))  # Port 587 for TLS, 465 for SSL
        FROM_EMAIL = os.getenv('FROM_EMAIL', 'monty@python.com')
        TO_EMAILS = ['jon@mycompany.com']
        SUBJECT = "Hello!"
        BODY = "This message was sent with Python's smtplib."

        # Call the function to send the email
        send_mail(FROM_EMAIL, TO_EMAILS, SUBJECT, BODY, SMTP_SERVER, SMTP_PORT)

    except Exception as e:
        logging.error(f"An error occurred: {e}")
