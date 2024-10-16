<?php

// Define your ID and API Key
$id = 'your_id';
$api_key = 'your_api_key';

// Create the basic auth string by combining the id and api_key, and encoding in base64
$auth_string = base64_encode("$id:$api_key");

// Set the target URL
$url = "https://api.domain.com/products?limit=1&offset=0";

// Initialize the cURL session
$curl = curl_init();

// Set the cURL options
curl_setopt_array($curl, [
    CURLOPT_URL => $url, 
    CURLOPT_RETURNTRANSFER => true, 
    CURLOPT_HTTPHEADER => [
        "Accept: application/product+xml", 
        "Authorization: Basic $auth_string" 
    ],
    CURLOPT_SSL_VERIFYPEER => true, 
    CURLOPT_SSL_VERIFYHOST => 2, 
]);

// Execute the cURL request
$response = curl_exec($curl);

// Check for any errors during the request
if ($response === false) {
    // Log the cURL error (secure logging should be done in production, avoid exposing sensitive data)
    $error_msg = curl_error($curl);
    error_log("cURL Error: $error_msg");
    echo "Request failed. Please check your credentials and try again.";
} else {
    // Parse the response
    $http_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);

    // Check if the HTTP status code indicates success
    if ($http_code === 200) {
        // Output the successful response (ensure proper output sanitization if needed)
        echo "Response: " . htmlspecialchars($response);
    } else {
        // Log the error message and HTTP code
        error_log("Request failed with HTTP code $http_code. Response: $response");
        echo "Request failed. HTTP Status Code: $http_code.";
    }
}

// Close the cURL session
curl_close($curl);

?>
