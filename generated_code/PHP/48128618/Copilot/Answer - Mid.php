<?php
// Include PHPMailer classes
require_once 'path_to_PHPMailer/src/Exception.php';
require_once 'path_to_PHPMailer/src/PHPMailer.php';
require_once 'path_to_PHPMailer/src/SMTP.php';

// Use the PHPMailer namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Create a new PHPMailer instance
$mail = new PHPMailer;

// Set up SMTP
$mail->isSMTP();
$mail->SMTPDebug = 2;
$mail->Host = 'smtp.gmail.com';
$mail->Port = 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPAuth = true;
$mail->Username = $smtpUsername;
$mail->Password = $smtpPassword;

// Set email parameters
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName);
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML('test body');
$mail->AltBody = 'HTML messaging not supported';

// Send the email
if (!$mail->send()) {
    echo 'Mailer Error: ' . $mail->ErrorInfo;
} else {
    echo 'Message sent!';
}
