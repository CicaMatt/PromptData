<?php
// Include PHPMailer library files
require 'path_to_PHPMailer/src/Exception.php';
require 'path_to_PHPMailer/src/PHPMailer.php';
require 'path_to_PHPMailer/src/SMTP.php';

// Use the PHPMailer namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Create a new PHPMailer instance
$mail = new PHPMailer;

// SMTP configuration
$mail->isSMTP();
$mail->SMTPDebug = 2;
$mail->Host = 'smtp.gmail.com';
$mail->Port = 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPAuth = true;
$mail->Username = $smtpUsername;
$mail->Password = $smtpPassword;

// Email settings
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName);
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML('test body');
$mail->AltBody = 'HTML messaging not supported';

// Send email
if (!$mail->send()) {
    echo 'Mailer Error: ' . $mail->ErrorInfo;
} else {
    echo 'Message sent!';
}
?>
