<?php
// Function to make a POST request and fetch content securely
function fetchPostContent($url, $postData = []) {
    // Sanitize and encode POST data
    $postData = http_build_query($postData);

    // Stream context for POST request
    $options = [
        'http' => [
            'method'  => 'POST',
            'header'  => 'Content-Type: application/x-www-form-urlencoded',
            'content' => $postData,
            'timeout' => 30,  // Timeout after 30 seconds
            'ignore_errors' => true  // Fetch response even on failure
        ]
    ];

    // Create context resource for the request
    $context = stream_context_create($options);

    // Fetch the result with file_get_contents (safe for use over HTTPS)
    $result = @file_get_contents($url, false, $context);

    // Error handling in case the request fails
    if ($result === FALSE) {
        throw new Exception("Error fetching the URL: $url");
    }

    return $result;
}

// Function to parse content using DOMDocument
function parseHtmlContent($html) {
    $dom = new DOMDocument();

    // Suppress warnings due to invalid HTML structure
    libxml_use_internal_errors(true);
    $dom->loadHTML($html);
    libxml_clear_errors();

    // Process the DOMDocument here (e.g., find specific elements)
    // Example: Extract all paragraphs
    $paragraphs = [];
    foreach ($dom->getElementsByTagName('p') as $node) {
        $paragraphs[] = $node->textContent;
    }

    return $paragraphs; // Return the extracted data
}

// Usage Example
try {
    // URL and POST data
    $url = 'https://example.com/search';
    $postData = ['query' => 'your_search_term'];

    // Fetch and parse content
    $htmlContent = fetchPostContent($url, $postData);
    $parsedData = parseHtmlContent($htmlContent);

    // Display parsed content
    foreach ($parsedData as $paragraph) {
        echo '<p>' . htmlspecialchars($paragraph) . '</p>';
    }

} catch (Exception $e) {
    // Display error message
    echo 'Error: ' . htmlspecialchars($e->getMessage());
}
