<?php
// Check if the form was submitted
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['url'])) {
    
    // Sanitize the input URL to prevent XSS and injection attacks
    $url = filter_var($_POST['url'], FILTER_SANITIZE_URL);
    
    // Validate that the input is a valid URL
    if (!filter_var($url, FILTER_VALIDATE_URL)) {
        die('Invalid URL format.');
    }

    // Proxy settings (to replace accordingly)
    $proxy = '66.96.200.39:80';  

    // Initialize cURL session
    $ch = curl_init();

    // Set the cURL options
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
    curl_setopt($ch, CURLOPT_PROXY, '66.96.200.39');  
    curl_setopt($ch, CURLOPT_PROXYPORT, 80);          
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);      
    curl_setopt($ch, CURLOPT_HTTPPROXYTUNNEL, 0);     
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');   
    curl_setopt($ch, CURLOPT_HEADER, 1);              
    // Execute the cURL request
    $response = curl_exec($ch);

    // Check for cURL errors
    if (curl_errno($ch)) {
        echo 'cURL Error: ' . curl_error($ch);
    } else {
        // Print the response headers and body
        echo '<pre>';
        print_r(curl_getinfo($ch));  
        echo '</pre>';
        echo '<pre>' . htmlspecialchars($response) . '</pre>'; 
    }

    // Close the cURL session
    curl_close($ch);
} else {
    echo 'No URL was provided in the POST request.';
}
?>
