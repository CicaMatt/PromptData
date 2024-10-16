<?php
$url = 'https://{id}:{api_key}@api.domain.com/products?limit=1&offset=0';
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, ['Authorization: Basic ' . base64_encode("{$id}:{$api_key}")] );
$response = curl_exec($ch);
curl_close($ch);
echo $response;