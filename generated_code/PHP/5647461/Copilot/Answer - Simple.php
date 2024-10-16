<?php
// URL to send the POST request to
$url = 'https://example.com/api';

// Data to be sent in the POST request
$data = array(
    'param1' => 'value1',
    'param2' => 'value2'
);

// Initialize cURL session
$ch = curl_init($url);

// Set cURL options
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));

// Execute cURL session and get the response
$response = curl_exec($ch);

// Close cURL session
curl_close($ch);

// Load the response into DOMDocument
$dom = new DOMDocument();
@$dom->loadHTML($response);

// Do something with the DOMDocument, e.g., print the contents
echo $dom->saveHTML();
?>
