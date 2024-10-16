<?php
// Ensure the URL is properly sanitized to prevent security vulnerabilities
$url = filter_var($_POST['url'], FILTER_VALIDATE_URL);

if ($url === false) {
    die('Invalid URL');
}

$proxy = '66.96.200.39:80';
$proxy_parts = explode(':', $proxy);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_PROXY, $proxy_parts[0]);
curl_setopt($ch, CURLOPT_PROXYPORT, $proxy_parts[1]);
curl_setopt($ch, CURLOPT_HEADER, true);

// Execute the cURL request
$response = curl_exec($ch);

if ($response === false) {
    echo 'cURL Error: ' . curl_error($ch);
} else {
    // Print the response and cURL info
    echo $response;
    print_r(curl_getinfo($ch));
}

// Close the cURL session
curl_close($ch);
?>
