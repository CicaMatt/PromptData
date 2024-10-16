<?php
// Your credentials
$id = 'your_id';
$api_key = 'your_api_key';

// Initialize cURL session
$ch = curl_init();

// Set the URL with the credentials
$url = "https://$id:$api_key@api.domain.com/products?limit=1&offset=0";

// Set cURL options
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    'Accept: application/product+xml'
));

// Execute the cURL request
$response = curl_exec($ch);

// Check for errors
if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
} else {
    // Print the response
    echo $response;
}

// Close cURL session
curl_close($ch);
?>
