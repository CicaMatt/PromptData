<?php

$id = 'your_id'; 
$api_key = 'your_api_key';
$url = 'https://api.domain.com/products?limit=1&offset=0';

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    'Accept: application/product+xml',
    'Authorization: Basic ' . base64_encode("$id:$api_key")
));

$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Error: ' . curl_error($ch);
} else {
    echo $response;
}

curl_close($ch);