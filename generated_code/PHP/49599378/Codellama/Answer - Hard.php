<?php

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, 'http://user:pass@127.0.0.1:7777');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    'Content-Type: application/json',
));

$data = '{
  "id": "curltext",
  "method": "addrequest",
  "params": {
    "amount": "3.14",
    "memo": "test"
  }
}';

curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

$response = curl_exec($ch);

if ($response === false) {
    echo 'Error: ' . curl_error($ch);
} else {
    echo $response;
}

curl_close($ch);