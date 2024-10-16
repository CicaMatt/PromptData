<?php
// Your credentials
$id = 'your_id';
$api_key = 'your_api_key';

// Initialize a cURL session
$ch = curl_init();

// Set the URL with parameters
$url = 'https://api.domain.com/products?limit=1&offset=0';

// Set the cURL options
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Set HTTP header, including Accept header
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Accept: application/product+xml',
]);

// Use CURLOPT_USERPWD to pass the id and api_key
curl_setopt($ch, CURLOPT_USERPWD, "$id:$api_key");

// Execute the cURL request
$response = curl_exec($ch);

// Check for errors
if (curl_errno($ch)) {
    echo 'Error: ' . curl_error($ch);
} else {
    // Handle the response here
    echo $response;
}

// Close the cURL session
curl_close($ch);
?>
