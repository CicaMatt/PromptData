<?php
// Data to send in the POST request
$data = array(
    'secret' => "my-app-secret",  // Replace with your actual reCAPTCHA secret
    'response' => "the-response"  // Replace with the user's response token
);

// Initialize cURL session
$verify = curl_init();

// Set cURL options
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);
curl_setopt($verify, CURLOPT_SSL_VERIFYPEER, true);  // Enable SSL verification for security

// Execute the request and fetch the response
$response = curl_exec($verify);

// Check for cURL errors
if ($response === false) {
    $error = curl_error($verify);
    curl_close($verify);
    die('cURL error: ' . $error);
}

// Close cURL session
curl_close($verify);

// Attempt to decode the JSON response
$responseData = json_decode($response, true);

// Check if JSON decoding was successful
if (json_last_error() !== JSON_ERROR_NONE) {
    die('Error decoding JSON: ' . json_last_error_msg());
}

// Output the response data
var_dump($responseData);
?>
