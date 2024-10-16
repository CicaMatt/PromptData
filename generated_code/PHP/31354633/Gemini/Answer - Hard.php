<?php

$data = array(
    'secret' => "my-app-secret",
    'response' => "the-response"
);

$verify = curl_init();

// Set the URL for the reCAPTCHA verification endpoint
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");

// Use POST method
curl_setopt($verify, CURLOPT_POST, true);

// Encode data for POST request
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));

// Return the transfer as a string
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);

// Additional options for better error handling and security

// Set a reasonable timeout to prevent hanging
curl_setopt($verify, CURLOPT_TIMEOUT, 30); // 30 seconds

// Include headers for better compatibility and debugging
curl_setopt($verify, CURLOPT_HTTPHEADER, array(
    'Content-Type: application/x-www-form-urlencoded'
));

// Enable verbose output for debugging (comment out in production)
// curl_setopt($verify, CURLOPT_VERBOSE, true);

// Capture any errors that occur
$response = curl_exec($verify);
$error = curl_error($verify);

// Close the cURL resource
curl_close($verify);

// Check for errors and handle them gracefully
if ($response === false) {
    // Log the error for further investigation (use a proper logging mechanism in production)
    error_log("cURL Error: " . $error);

    // Provide a user-friendly error message or take appropriate action
    die("An error occurred while verifying reCAPTCHA. Please try again later.");
}

// Decode the JSON response
$responseData = json_decode($response, true);

// Check if JSON decoding was successful
if ($responseData === null) {
    // Log the error 
    error_log("JSON Decoding Error: " . json_last_error_msg());

    // Handle the error gracefully
    die("An error occurred while processing the reCAPTCHA response. Please try again later.");
}

// Now you can work with the decoded JSON data
var_dump($responseData);
