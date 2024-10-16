<?php
// Sanitize user input to prevent vulnerabilities like XSS and SQL Injection
$url = filter_var($_POST['url'], FILTER_SANITIZE_URL); 

if (filter_var($url, FILTER_VALIDATE_URL) === false) {
    die('Invalid URL provided.'); 
}

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);

// Proxy configuration
curl_setopt($ch, CURLOPT_PROXY, '66.96.200.39');
curl_setopt($ch, CURLOPT_PROXYPORT, 80);

// Additional options
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1); 
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // Return the response instead of outputting it
curl_setopt($ch, CURLOPT_HEADER, 1); // Include headers in the response

$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Curl error: ' . curl_error($ch);
} else {
    // Process the response as needed
    // ...

    // Output debug information (remove in production)
    print_r(curl_getinfo($ch));
    echo $response; 
}

curl_close($ch);
?>