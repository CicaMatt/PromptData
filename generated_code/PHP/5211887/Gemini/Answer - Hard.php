<?php
// Sanitize user input to prevent vulnerabilities
$url = filter_var($_POST['url'], FILTER_VALIDATE_URL); 

if (!$url) {
    die('Invalid URL provided.'); 
}

$proxy = '66.96.200.39:80'; // Ensure the proxy details are secure and valid

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
curl_setopt($ch, CURLOPT_PROXY, $proxy);
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1); 
curl_setopt($ch, CURLOPT_HEADER, 1);         

// Error handling to provide informative feedback
$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Curl error: ' . curl_error($ch);
} else {
    // Process the response as needed
    echo $response; 
}

curl_close($ch);
?>