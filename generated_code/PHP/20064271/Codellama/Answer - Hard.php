<?php
$url = "https://{id}:{api_key}@api.domain.com/products?limit=1&offset=0";
$headers = array(
    'Accept: application/product+xml',
    'Authorization: Basic {id}:{api_key}'
);

$curl = curl_init($url);
curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($curl);
curl_close($curl);

print_r($response);