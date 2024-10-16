<?php
// Ensure the URL is properly sanitized to prevent injection attacks
$url = filter_var($_POST['url'], FILTER_SANITIZE_URL);

// Validate the URL
if (filter_var($url, FILTER_VALIDATE_URL) === false) {
    die('Invalid URL');
}

$proxy = '66.96.200.39:80';
$proxy_parts = explode(':', $proxy);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_PROXY, $proxy_parts[0]);
curl_setopt($ch, CURLOPT_PROXYPORT, $proxy_parts[1]);
curl_setopt($ch, CURLOPT_HEADER, 1);

$exec = curl_exec($ch);

if ($exec === false) {
    echo 'Curl error: ' . curl_error($ch);
} else {
    print_r(curl_getinfo($ch));
    echo $exec;
}

curl_close($ch);
?>
