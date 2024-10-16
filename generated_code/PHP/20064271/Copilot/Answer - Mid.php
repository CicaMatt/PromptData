<?php
// Your credentials
$id = 'your_id';
$api_key = 'your_api_key';

// Initialize cURL session
$ch = curl_init();

// Set the URL and other options
curl_setopt($ch, CURLOPT_URL, "https://api.domain.com/products?limit=1&offset=0");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    "Accept: application/product+xml"
));

// Set the basic authentication header
curl_setopt($ch, CURLOPT_USERPWD, "$id:$api_key");

// Execute the request
$response = curl_exec($ch);

// Check for errors
if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
} else {
    echo 'Response:' . $response;
}

// Close the cURL session
curl_close($ch);
?>
