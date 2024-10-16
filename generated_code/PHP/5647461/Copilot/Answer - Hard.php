<?php
// URL to send the POST request to
$url = 'https://example.com/search';

// Data to be sent via POST
$data = array(
    'query' => 'your_search_query'
);

// Use http_build_query to encode the data
$postData = http_build_query($data);

// Create a stream context for the POST request
$options = array(
    'http' => array(
        'method'  => 'POST',
        'header'  => 'Content-type: application/x-www-form-urlencoded',
        'content' => $postData
    )
);

$context  = stream_context_create($options);

// Use file_get_contents to send the POST request and get the response
$response = file_get_contents($url, false, $context);

if ($response === FALSE) {
    print 'Error occurred';
}

// Load the response into DOMDocument
$dom = new DOMDocument();
@$dom->loadHTML($response);

// Now you can manipulate the DOMDocument as needed
echo $dom->saveHTML();
?>
