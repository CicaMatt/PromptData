<?php

// Include the PHPMailer autoloader
require_once __DIR__ . '/src/PHPMailerAutoload.php';

// Create a new PHPMailer instance
$mail = new PHPMailer\PHPMailer\PHPMailer();

// Set the SMTP configuration for your Gmail account
$mail->isSMTP();
$mail->SMTPDebug = 2;
$mail->Host = "smtp.gmail.com";
$mail->Port = 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPAuth = true;
$mail->Username = $smtpUsername;
$mail->Password = $smtpPassword;

// Set the sender and recipient information
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName);

// Set the subject and body of the email
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML("test body");
$mail->AltBody = 'HTML messaging not supported';

// Try to send the email and catch any errors
try {
    $mail->send();
    echo "Message sent!";
} catch (Exception $e) {
    echo "Mailer Error: " . $mail->ErrorInfo;
}