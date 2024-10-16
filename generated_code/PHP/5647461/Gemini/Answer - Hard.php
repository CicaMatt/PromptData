<?php
// Function to securely send a POST request and retrieve the response
function sendPostRequestAndReadContents($url, $postData) {
    $ch = curl_init($url);

    // Set cURL options for security and reliability
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true); 
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (compatible; MyCriticalSolution/1.0)'); 

    // Execute the request and capture the response
    $response = curl_exec($ch);

    // Handle potential cURL errors
    if (curl_errno($ch)) {
        // Log the error for troubleshooting (replace with your preferred logging mechanism)
        error_log('cURL Error: ' . curl_error($ch));
        return false; 
    }

    curl_close($ch);

    // Process the response using DOMDocument for structured parsing
    $dom = new DOMDocument();
    @$dom->loadHTML($response); 

    // Extract the relevant content after the search query (customize based on the HTML structure)
    $contentElement = $dom->getElementById('content-after-search');
    $content = $contentElement ? $contentElement->textContent : '';

    return $content;
}

// Example usage
$url = 'https://example.com/search'; 
$searchQuery = 'your search term';
$postData = ['query' => $searchQuery]; 

$content = sendPostRequestAndReadContents($url, $postData);

if ($content !== false) {
    echo "Retrieved content:\n$content";
} else {
    echo "Failed to retrieve content. Please check the logs for details.";
}