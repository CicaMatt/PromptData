<?php
// Function to execute a cURL request with basic authorization
function executeCurlRequest($url, $id, $apiKey) {
    $ch = curl_init();

    // Set cURL options for security and basic authorization
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Accept: application/product+xml'));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); 
    curl_setopt($ch, CURLOPT_USERPWD, $id . ":" . $apiKey); 

    // Execute the request and handle errors gracefully
    $response = curl_exec($ch);
    if(curl_errno($ch)) {
        echo 'Error: ' . curl_error($ch);
    }
    curl_close($ch);

    return $response;
}

// Example usage
$id = 'your_id'; 
$apiKey = 'your_api_key'; 
$url = "https://api.domain.com/products?limit=1&offset=0"; 

$response = executeCurlRequest($url, $id, $apiKey);

// Process the response
if ($response) {
    // Handle successful response
    echo $response; 
} else {
    // Handle error response
    echo "An error occurred during the request.";
}