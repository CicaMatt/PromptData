import smtplib

def sendMail(server, FROM, TO, SUBJECT, TEXT):
    """Sends an email using the provided SMTP server connection."""
    message = f"""\
From: {FROM}
To: {", ".join(TO)}
Subject: {SUBJECT}

{TEXT}
"""
    server.sendmail(FROM, TO, message)

# Establish the SMTP connection once
server = smtplib.SMTP('myserver')  # Replace 'myserver' with your actual server address

# Example usage
sender = 'monty@python.com'
recipients = ["jon@mycompany.com"] 
subject = "Hello!"
body = "This message was sent with Python's smtplib."

sendMail(server, sender, recipients, subject, body)

# Close the connection when done
server.quit()