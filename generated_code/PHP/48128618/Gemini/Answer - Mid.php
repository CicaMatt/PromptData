<?php
// Ensure you've downloaded the PHPMailer library and placed it in a directory named "PHPMailer"
// This directory should contain the necessary files like "src/PHPMailer.php", etc.

// Include the necessary files from the PHPMailer library
require_once("PHPMailer/src/PHPMailer.php");
require_once("PHPMailer/src/SMTP.php");
require_once("PHPMailer/src/Exception.php");

// Use the PHPMailer classes with their full namespace
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

// ... rest of your code (instantiating PHPMailer, setting up SMTP, etc.) ...
$mail = new PHPMailer(true); // Enable exceptions for error handling
// ...

// Error handling with try-catch
try {
    if (!$mail->send()) {
        echo "Mailer Error: " . $mail->ErrorInfo;
    } else {
        echo "Message sent!";
    }
} catch (Exception $e) {
    echo "Mailer Error: " . $e->getMessage();
}