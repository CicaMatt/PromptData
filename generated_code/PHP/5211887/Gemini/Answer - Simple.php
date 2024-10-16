<?php
$url = $_POST[1]; // Corrected variable interpolation
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HTTPPROXYTUNNEL, 0);
curl_setopt($ch, CURLOPT_PROXY, '66.96.200.39:80');
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // Capture the output
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($ch, CURLOPT_HEADER, 1); // Added missing semicolon
$output = curl_exec($ch); 
$curl_info = curl_getinfo($ch);
curl_close($ch);

// Check for errors
if ($output === false) {
    echo 'cURL Error: ' . curl_error($ch);
} else {
    echo '<br />';
    print_r($curl_info);
    echo $output; // Output the result
}
?>