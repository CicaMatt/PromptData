<?php

// Use environment variables to store sensitive data like API keys
$secretKey = getenv('RECAPTCHA_SECRET_KEY');
$responseKey = isset($_POST['g-recaptcha-response']) ? $_POST['g-recaptcha-response'] : null;

// Validate that required fields are not empty
if (empty($secretKey) || empty($responseKey)) {
    die("Missing required parameters.");
}

// Prepare data for the POST request
$data = [
    'secret' => $secretKey,
    'response' => $responseKey,
    'remoteip' => $_SERVER['REMOTE_ADDR'] // Include the user's IP for added security
];

// Initialize cURL session
$verify = curl_init();

// Set cURL options securely
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);

// Enable SSL verification for security (disable only in a local development environment)
curl_setopt($verify, CURLOPT_SSL_VERIFYPEER, true);
curl_setopt($verify, CURLOPT_SSL_VERIFYHOST, 2);

// Execute the cURL request and capture the response
$response = curl_exec($verify);

// Error handling: Check for cURL errors
if ($response === false) {
    $curlError = curl_error($verify);
    curl_close($verify);
    die("cURL error: " . htmlspecialchars($curlError, ENT_QUOTES, 'UTF-8'));
}

// Close the cURL session
curl_close($verify);

// Decode the JSON response into an associative array
$responseData = json_decode($response, true);

// Verify the success of the reCAPTCHA response
if ($responseData && isset($responseData['success']) && $responseData['success'] == true) {
    echo "reCAPTCHA verification succeeded.";
    // Proceed with further actions if necessary
} else {
    echo "reCAPTCHA verification failed.";
    // Handle failure (log the event, notify the user, etc.)
}

// Optionally, sanitize and output the full response for debugging (development only)
// echo htmlspecialchars(json_encode($responseData, JSON_PRETTY_PRINT), ENT_QUOTES, 'UTF-8');
?>
