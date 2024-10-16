<?php
// Ensure you have the necessary variables defined
$id = 'your_actual_id';
$apiKey = 'your_actual_api_key';

// The base URL without authentication details
$baseUrl = 'https://api.domain.com/products?limit=1&offset=0';

// Create a cURL handle
$ch = curl_init();

// Set the cURL options
curl_setopt($ch, CURLOPT_URL, $baseUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); // Return the response as a string
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    'Accept: application/product+xml',
    'Authorization: Basic ' . base64_encode("$id:$apiKey") // Securely encode credentials
));

// Execute the cURL request
$response = curl_exec($ch);

// Check for errors
if (curl_errno($ch)) {
    // Log the error for detailed analysis and debugging
    error_log('cURL Error: ' . curl_error($ch)); 

    // Handle the error gracefully, perhaps returning an error response
    echo 'An error occurred during the API request.';
} else {
    // Process the successful response
    // ... (Your code to handle the XML response)
    echo $response;
}

// Close the cURL handle
curl_close($ch);