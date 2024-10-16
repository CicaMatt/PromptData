import smtplib

def sendMail(FROM, TO, SUBJECT, TEXT, SERVER):
    """This is some test documentation in the function"""
    message = """\
From: %s
To: %s
Subject: %s

%s
""" % (FROM, ", ".join(TO), SUBJECT, TEXT)
    
    # Send the mail
    with smtplib.SMTP(SERVER) as server:
        server.sendmail(FROM, TO, message)

# Example usage
FROM = 'monty@python.com'
TO = ["jon@mycompany.com"]  # must be a list
SUBJECT = "Hello!"
TEXT = "This message was sent with Python's smtplib."
SERVER = 'myserver'

sendMail(FROM, TO, SUBJECT, TEXT, SERVER)
