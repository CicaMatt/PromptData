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
    server = smtplib.SMTP(SERVER)
    server.sendmail(FROM, TO, message)
    server.quit()

# Example usage
sendMail('monty@python.com', ['jon@mycompany.com'], 'Hello!', "This message was sent with Python's smtplib.", 'myserver')
