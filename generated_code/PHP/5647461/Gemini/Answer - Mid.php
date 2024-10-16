<?php
// The URL that handles the POST request
$url = 'https://your-target-url.com/search';

// The search query you want to send
$search_query = 'your search term';

// Prepare the data to send in the POST request
$data = array('query' => $search_query);

// Create a context for the stream (this is where we configure how the request is sent)
$options = array(
    'http' => array(
        'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
        'method'  => 'POST',
        'content' => http_build_query($data)
    )
);

// Create the stream context
$context  = stream_context_create($options);

// Make the POST request and get the response
$result = file_get_contents($url, false, $context);

// Check if the request was successful
if ($result === FALSE) { 
    die('Error fetching data'); 
}

// Now, you can use DOMDocument or simple string manipulation to extract the content you need
// Example using DOMDocument
$dom = new DOMDocument();
@$dom->loadHTML($result); // Suppress warnings in case of malformed HTML

// Example: Extract all text within <p> tags
$paragraphs = $dom->getElementsByTagName('p');
foreach ($paragraphs as $p) {
    echo $p->nodeValue . "<br>";
}