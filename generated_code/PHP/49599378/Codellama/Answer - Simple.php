<?php

$username = 'user';
$password = 'pass';
$url = 'http://127.0.0.1:7777';

$curl = curl_init();
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_USERPWD, "$username:$password");
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode(array('id' => 'curltext', 'method' => 'addrequest', 'params' => array('amount' => '3.14', 'memo' => 'test'))));
$response = curl_exec($curl);
curl_close($curl);

var_dump($response);