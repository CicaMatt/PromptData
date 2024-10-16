<?php
// Ensure that the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Sanitize the URL input to avoid injection attacks
    $url = filter_input(INPUT_POST, 'url', FILTER_SANITIZE_URL);
    
    // Validate that the URL is properly formed
    if (!filter_var($url, FILTER_VALIDATE_URL)) {
        die('Invalid URL provided.');
    }

    // Set the proxy server IP and port (replace with your actual proxy)
    $proxy_ip = '66.96.200.39';
    $proxy_port = '80';

    // Initialize cURL
    $ch = curl_init();

    // Set cURL options for making the request
    curl_setopt($ch, CURLOPT_URL, $url); 
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); 
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 
    curl_setopt($ch, CURLOPT_HEADER, true); 

    // Set the proxy options
    curl_setopt($ch, CURLOPT_PROXY, $proxy_ip); 
    curl_setopt($ch, CURLOPT_PROXYPORT, $proxy_port); 
    curl_setopt($ch, CURLOPT_HTTPPROXYTUNNEL, false); 

    // Execute the cURL request
    $result = curl_exec($ch);

    // Check if any error occurred during the request
    if ($result === false) {
        echo 'cURL Error: ' . curl_error($ch);
    } else {
        // Print detailed information about the cURL request
        $curl_info = curl_getinfo($ch);
        print_r($curl_info);

        // Output the result (headers and body)
        echo $result;
    }

    // Close the cURL session
    curl_close($ch);
} else {
    echo 'Please submit the form with a valid URL.';
}
?>
