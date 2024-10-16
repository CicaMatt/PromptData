import smtplib

def sendMail(FROM, TO, SUBJECT, TEXT, SERVER):
    message = """From: %s
    To: %s
    Subject: %s
    %s
    """ % (FROM, ", ".join(TO), SUBJECT, TEXT)
    server.sendmail(FROM, TO, message)

# Use the function
server = smtplib.SMTP('myserver')
sendMail('monty@python.com', ['jon@mycompany.com'], 'Hello!', 'This is a test email sent with Python\'s smtplib.', server)