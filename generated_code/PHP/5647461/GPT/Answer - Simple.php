<?php

// The URL where you want to send the POST request
$url = 'https://example.com/api/endpoint';

// The POST data you want to send (in the form of an array or a query string)
$data = array(
    'param1' => 'value1',
    'param2' => 'value2'
);

// Convert the POST data to a query string format
$postData = http_build_query($data);

// Set up the HTTP context with POST method and content
$options = array(
    'http' => array(
        'header'  => "Content-type: application/x-www-form-urlencoded\r\n", // Optional: Add headers if needed
        'method'  => 'POST',
        'content' => $postData,
    ),
);

// Create a stream context with the options
$context = stream_context_create($options);

// Use file_get_contents() to send the POST request and get the response
$response = file_get_contents($url, false, $context);

if ($response === FALSE) {
    // Handle errors
    die('Error occurred while making the POST request');
}

// Output the response
echo $response;
?>
