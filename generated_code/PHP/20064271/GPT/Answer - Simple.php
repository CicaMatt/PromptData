<?php

$id = "your_id"; // replace with your actual ID
$api_key = "your_api_key"; // replace with your actual API key
$url = "https://api.domain.com/products?limit=1&offset=0"; // replace with your actual URL

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// This will automatically handle the Base64 encoding for you
curl_setopt($ch, CURLOPT_USERPWD, "$id:$api_key");

// Set headers
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    "Accept: application/product+xml"
));

$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
} else {
    echo $response;
}

curl_close($ch);
