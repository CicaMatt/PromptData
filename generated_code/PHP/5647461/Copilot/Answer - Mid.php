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
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Execute the POST request
$response = curl_exec($ch);

// Check for cURL errors
if ($response === false) {
    echo 'cURL Error: ' . curl_error($ch);
} else {
    // Load the response into a DOMDocument
    $dom = new DOMDocument();
    @$dom->loadHTML($response);

    // Do something with the DOMDocument, e.g., read specific elements
    $elements = $dom->getElementsByTagName('p');
    foreach ($elements as $element) {
        echo $element->nodeValue . "\n";
    }
}

// Close the cURL session
curl_close($ch);
?>
