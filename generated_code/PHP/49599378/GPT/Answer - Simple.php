<?php
$url = "http://127.0.0.1:7777/";
$data = json_encode([
    'id' => 'curltext',
    'method' => 'addrequest',
    'params' => [
        'amount' => '3.14',
        'memo' => 'test'
    ]
]);

$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

// Basic Authentication
curl_setopt($ch, CURLOPT_USERPWD, "user:pass");
curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Content-Type: application/json',
]);

$response = curl_exec($ch);

if ($response === false) {
    $error = curl_error($ch);
    echo "Curl Error: $error";
} else {
    echo "Response: $response";
}

curl_close($ch);
