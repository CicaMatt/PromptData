<?php

$url = $_POST[1];
$proxy = "66.96.200.39:80";

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_PROXY, $proxy[0]);
curl_setopt($ch, CURLOPT_PROXYPORT, $proxy[1]);
curl_setopt($ch, CURLOPT_HEADER, 1);

$exec = curl_exec($ch);

echo curl_error($ch);
print_r(curl_getinfo($ch));
echo $exec;