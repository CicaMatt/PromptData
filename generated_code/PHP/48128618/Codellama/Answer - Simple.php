<?php

// Include the necessary files
require_once("src/PHPMailer.php");
require_once("src/SMTP.php");
require_once("src/Exception.php");

// Set up the mail parameters
$mail = new PHPMailer;
$mail->isSMTP(); 
$mail->Host = "smtp.gmail.com"; 
$mail->Port = 587;
$mail->SMTPSecure = 'tls';
$mail->SMTPAuth = true;
$mail->Username = $smtpUsername;
$mail->Password = $smtpPassword;
$mail->setFrom($emailFrom, $emailFromName);
$mail->addAddress($emailTo, $emailToName);
$mail->Subject = 'PHPMailer GMail SMTP test';
$mail->msgHTML("test body"); 
$mail->AltBody = 'HTML messaging not supported';

if(!$mail->send()){
    echo "Mailer Error: " . $mail->ErrorInfo;
}else{
    echo "Message sent!";
}