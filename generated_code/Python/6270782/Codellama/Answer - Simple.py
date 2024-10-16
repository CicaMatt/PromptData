import smtplib

def sendMail(TO, SUBJECT, TEXT):
    FROM = 'monty@python.com' # or whatever value you want to specify
    server = smtplib.SMTP('myserver')
    message = """From: %s
    To: %s
    Subject: %s

    %s
    """ % (FROM, ", ".join(TO), SUBJECT, TEXT)
    server.sendmail(FROM, TO, message)
    server.quit()