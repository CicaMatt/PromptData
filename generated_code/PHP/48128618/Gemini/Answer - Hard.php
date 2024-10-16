<?php

// Explicitly include necessary PHPMailer files 
require_once("src/PHPMailer/src/PHPMailer.php");
require_once("src/PHPMailer/src/SMTP.php");
require_once("src/PHPMailer/src/Exception.php");

// Namespace usage for clarity and compatibility
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

// ... (Rest of your code remains the same) ...

$mail = new PHPMailer(true); // Enable exceptions for error handling

// ... (Continue with your email configuration and sending) ...

try {
    if (!$mail->send()) {
        echo "Mailer Error: " . $mail->ErrorInfo;
    } else {
        echo "Message sent!";
    }
} catch (Exception $e) {
    echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
}