<?php

// Replace with your Electrum RPC url, username and password
$url = 'http://127.0.0.1:7777'; 
$data = '{"id":"curltext","method":"addrequest","params":{"amount":"3.14","memo":"test"}}';
$username = 'user'; 
$password = 'pass'; 

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Set Basic Authentication headers
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($ch, CURLOPT_USERPWD, $username . ":" . $password);

// Additional security and error handling
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true); 
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 2); 

$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'cURL Error: ' . curl_error($ch);
} else {
    echo 'Response from Electrum: ' . $response;
}

curl_close($ch);