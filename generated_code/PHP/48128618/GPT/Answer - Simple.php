<?php
// Include the required PHPMailer classes
require_once("src/PHPMailer.php");
require_once("src/SMTP.php");
require_once("src/Exception.php");

// Import the necessary PHPMailer classes into the global namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Create a new PHPMailer instance
$mail = new PHPMailer;

// Server settings
$mail->isSMTP();
$mail->SMTPDebug = 2; // Set to 0 for no output, 2 for full debug output
$mail->Host = "smtp.gmail.com"; // Specify main and backup SMTP servers
$mail->Port = 587; // TCP port to connect to
$mail->SMTPSecure = 'tls'; // Enable TLS encryption, `ssl` also accepted
$mail->SMTPAuth = true; // Enable SMTP authentication
$mail->Username = $smtpUsername; // SMTP username
$mail->Password = $smtpPassword; // SMTP password

// Recipients
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName); // Add a recipient

// Content
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML("test body"); // HTML body
$mail->AltBody = 'HTML messaging not supported'; // Plain text body for non-HTML mail clients

// Send the message, check for errors
if (!$mail->send()) {
    echo "Mailer Error: " . $mail->ErrorInfo;
} else {
    echo "Message sent!";
}
