<?php

$url = 'http://127.0.0.1:7777'; 
$data = '{"id":"curltext","method":"addrequest","params":{"amount":"3.14","memo":"test"}}';

$ch = curl_init();

// Set cURL options
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, 1); 
curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); 
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json')); 
curl_setopt($ch, CURLOPT_USERPWD, "user:pass"); 
// Execute the cURL request
$response = curl_exec($ch);

// Check for errors
if(curl_errno($ch)) {
    echo 'cURL Error: ' . curl_error($ch);
} else {
    echo 'Response: ' . $response;
}

// Close cURL handle
curl_close($ch);

?>