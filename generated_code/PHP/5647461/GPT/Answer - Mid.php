<?php
// URL to send the POST request to
$url = "https://example.com/your-post-endpoint"; // Replace with your target URL

// Data to send in the POST request
$postData = array(
    'param1' => 'value1',
    'param2' => 'value2',
    'param3' => 'value3'
);

// Initialize cURL session
$ch = curl_init();

// Set the URL for the cURL request
curl_setopt($ch, CURLOPT_URL, $url);

// Set the HTTP method to POST
curl_setopt($ch, CURLOPT_POST, true);

// Pass the POST fields (data) to the request
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($postData));

// Set the option to return the transfer as a string instead of outputting directly
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Execute the cURL request and store the response in $response
$response = curl_exec($ch);

// Check for cURL errors
if ($response === false) {
    $error = curl_error($ch);
    echo "cURL Error: $error";
} else {
    // Output the response from the POST request
    echo "Response: " . htmlspecialchars($response);
}

// Close the cURL session
curl_close($ch);
?>
