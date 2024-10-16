<?php
// Include the necessary PHPMailer classes using require_once
require_once 'src/PHPMailer.php';
require_once 'src/SMTP.php';
require_once 'src/Exception.php';

// Use the correct namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// SMTP credentials
$smtpUsername = 'your-smtp-username@gmail.com';
$smtpPassword = 'your-smtp-password';

// Email details
$emailFrom = 'your-email@gmail.com';
$emailFromName = 'Your Name';
$emailTo = 'recipient-email@example.com';
$emailToName = 'Recipient Name';

// Create a new PHPMailer instance
$mail = new PHPMailer();

try {
    // Tell PHPMailer to use SMTP
    $mail->isSMTP();

    // Enable SMTP debugging
    // 0 = off (for production use)
    // 1 = client messages
    // 2 = client and server messages
    $mail->SMTPDebug = 2;

    // Set the SMTP host
    $mail->Host = 'smtp.gmail.com';

    // Set the SMTP port
    $mail->Port = 587;

    // Set encryption type to TLS
    $mail->SMTPSecure = 'tls';

    // Enable SMTP authentication
    $mail->SMTPAuth = true;

    // Set the SMTP credentials
    $mail->Username = $smtpUsername;
    $mail->Password = $smtpPassword;

    // Set who the message is to be sent from
    $mail->setFrom($emailFrom, $emailFromName);

    // Set who the message is to be sent to
    $mail->addAddress($emailTo, $emailToName);

    // Set the subject
    $mail->Subject = 'PHPMailer GMail SMTP test';

    // Set the HTML body content
    $mail->msgHTML('This is a test email body');

    // Set the plain text alternative body for non-HTML email clients
    $mail->AltBody = 'This is a plain-text message body';

    // Send the message
    if (!$mail->send()) {
        // If the message couldn't be sent, throw an error
        throw new Exception('Mailer Error: ' . $mail->ErrorInfo);
    } else {
        echo 'Message sent!';
    }
} catch (Exception $e) {
    echo 'Caught Exception: ', $e->getMessage(), "\n";
}
