import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import ssl

def sendMail(FROM, TO, SUBJECT, TEXT, myserver, port=587, user=None, password=None):
    """Function to send an email with proper error handling and security"""

    # Create the email message
    message = MIMEMultipart()
    message["From"] = FROM
    message["To"] = ", ".join(TO)
    message["Subject"] = SUBJECT
    message.attach(MIMEText(TEXT, "plain"))

    # Convert message to string
    message_str = message.as_string()

    try:
        # Set up the secure SMTP connection
        context = ssl.create_default_context()
        
        with smtplib.SMTP(myserver, port) as server:
            server.ehlo()  
            server.starttls(context=context)  
            server.ehlo()  

            # If username and password are provided, log in
            if user and password:
                server.login(user, password)
            
            # Send the email
            server.sendmail(FROM, TO, message_str)

            print(f"Email sent successfully from {FROM} to {TO}")

    except smtplib.SMTPException as e:
        print(f"Failed to send email: {e}")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
