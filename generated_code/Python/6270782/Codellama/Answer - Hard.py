import smtplib
import time

def sendMail(FROM, TO, SUBJECT, TEXT, SERVER):
    import smtplib
    """this is some test documentation in the function"""
    
    # Try sending the email
    try:
        message = """From: %s
        To: %s
        Subject: %s
        %s
        """ % (FROM, ", ".join(TO), SUBJECT, TEXT)
        server = smtplib.SMTP(SERVER)
        server.sendmail(FROM, TO, message)
    except smtplib.SMTPServerDisconnected as e:
        # If the connection is closed, retry sending the email after 5 seconds
        print("Connection unexpectedly closed, retrying...")
        time.sleep(5)
        server = smtplib.SMTP(SERVER)
        server.sendmail(FROM, TO, message)
    finally:
        # Quit the SMTP session
        server.quit()