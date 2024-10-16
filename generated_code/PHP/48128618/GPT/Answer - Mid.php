<?php

// Load PHPMailer classes manually
require_once('src/PHPMailer.php');
require_once('src/SMTP.php');
require_once('src/Exception.php');

// Use namespaces
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Create a new PHPMailer instance
$mail = new PHPMailer();

// Server settings
$mail->isSMTP();
$mail->SMTPDebug = 2;
$mail->Host = "smtp.gmail.com";
$mail->Port = 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPAuth = true;
$mail->Username = $smtpUsername; // Your Gmail address
$mail->Password = $smtpPassword; // Your Gmail password

// Recipients
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName);

// Content
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML("test body");
$mail->AltBody = 'HTML messaging not supported';

// Send the email
if (!$mail->send()) {
    echo "Mailer Error: " . $mail->ErrorInfo;
} else {
    echo "Message sent!";
}
?>
