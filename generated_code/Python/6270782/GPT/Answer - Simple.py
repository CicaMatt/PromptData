def sendMail(FROM, TO, SUBJECT, TEXT, SERVER):
    import smtplib
    # Prepare the message
    message = """\
From: %s
To: %s
Subject: %s

%s
""" % (FROM, ", ".join(TO), SUBJECT, TEXT)

    # Send the mail
    try:
        with smtplib.SMTP(SERVER) as server:
            server.sendmail(FROM, TO, message)
    except smtplib.SMTPException as e:
        print(f"Error: {e}")
