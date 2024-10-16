import smtplib

def send_email(from_addr, to_addrs, subject, body, server_addr, port=None, username=None, password=None, use_tls=False):
    """
    Sends an email using the specified SMTP server.

    Args:
        from_addr: The sender's email address.
        to_addrs: A list of recipient email addresses.
        subject: The subject of the email.
        body: The body of the email.
        server_addr: The address of the SMTP server.
        port: The port of the SMTP server (optional, defaults to standard SMTP ports).
        username: The username for authentication (optional).
        password: The password for authentication (optional).
        use_tls: Whether to use TLS encryption (optional, defaults to False).
    """

    message = f"""\
From: {from_addr}
To: {", ".join(to_addrs)}
Subject: {subject}

{body}
"""

    try:
        server = smtplib.SMTP(server_addr, port)

        if use_tls:
            server.starttls()

        if username and password:
            server.login(username, password)

        server.sendmail(from_addr, to_addrs, message)
        print("Email sent successfully!")
    except smtplib.SMTPException as e:
        print(f"Error sending email: {e}")
    finally:
        server.quit()

# Example usage
if __name__ == "__main__":
    sender = 'monty@python.com'
    recipients = ["jon@mycompany.com"]
    subject = "Hello!"
    body = "This message was sent with Python's smtplib."
    server = 'your_smtp_server_address'  # Replace with your actual server address

    send_email(sender, recipients, subject, body, server)