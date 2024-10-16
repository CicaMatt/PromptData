<?php
$url = 'http://user:pass@127.0.0.1:7777';
$data = array('id' => 'curltext', 'method' => 'addrequest', 'params' => array('amount' => 3.14, 'memo' => 'test'));
$options = array(
    CURLOPT_URL => $url,
    CURLOPT_POSTFIELDS => json_encode($data),
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_HTTPHEADER => array('Content-Type: application/json')
);

$curl = curl_init();
curl_setopt_array($curl, $options);
$response = curl_exec($curl);
$err = curl_error($curl);

if ($err) {
    echo "cURL Error #:" . $err;
} else {
    echo $response;
}