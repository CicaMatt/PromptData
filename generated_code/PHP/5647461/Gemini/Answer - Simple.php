<?php
$url = 'https://your-post-only-url.com'; 
$data = array('param1' => 'value1', 'param2' => 'value2');

$ch = curl_init(); 

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
} else {
    // Process the $response using DOMDocument or other methods
    $dom = new DOMDocument();
    @$dom->loadHTML($response); 
    // ... further DOM processing 
}

curl_close($ch);
?>