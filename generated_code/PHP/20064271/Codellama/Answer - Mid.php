<?php
$curl = curl_init();

// Set the URL
$url = 'https://{id}:{api_key}@api.domain.com/products?limit=1&offset=0';
curl_setopt($curl, CURLOPT_URL, $url);

// Set the HTTP method to GET
curl_setopt($curl, CURLOPT_HTTPGET, true);

// Set the Accept header
$headers = array('Accept: application/product+xml');
curl_setopt($curl, CURLOPT_HEADER, $headers);

// Set the username and password for basic authentication
$username = '{id}';
$password = '{api_key}';
curl_setopt($curl, CURLOPT_USERPWD, "$username:$password");

// Send the request and save the response
$response = curl_exec($curl);

// Close the cURL session
curl_close($curl);