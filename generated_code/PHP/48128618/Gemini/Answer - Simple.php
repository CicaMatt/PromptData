<?php

// Include the necessary PHPMailer files directly
require_once("src/PHPMailer.php");
require_once("src/SMTP.php");
require_once("src/Exception.php");

// Use the correct namespace when creating the PHPMailer object
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

$mail = new PHPMailer(true); // Enable exceptions for error handling

// ... (Rest of your email configuration and sending code)

try {
    if (!$mail->send()) {
        echo "Mailer Error: " . $mail->ErrorInfo;
    } else {
        echo "Message sent!";
    }
} catch (Exception $e) {
    echo "Mailer Error: " . $e->getMessage();
}