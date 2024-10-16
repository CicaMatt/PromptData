import smtplib

def sendMail(FROM, TO, SUBJECT, TEXT, SERVER, USERNAME=None, PASSWORD=None):
    message = """\
        From: %s
        To: %s
        Subject: %s
        %s
""" % (FROM, ", ".join(TO), SUBJECT, TEXT)


    try:
        server = smtplib.SMTP(SERVER)

        # If username and password are provided, attempt to log in
        if USERNAME and PASSWORD:
            server.login(USERNAME, PASSWORD) 

        server.sendmail(FROM, TO, message)
    except smtplib.SMTPAuthenticationError:
        print("Authentication failed. Please check your username and password.")
    except smtplib.SMTPException as e:
        print("An error occurred while sending the email:", e)
    finally:
        server.quit()