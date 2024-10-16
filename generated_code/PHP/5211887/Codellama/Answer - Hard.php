<?php
$url = $_POST['1'];
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL,$url);
curl_setopt($ch, CURLOPT_HTTPPROXYTUNNEL, 0);
curl_setopt($ch, CURLOPT_PROXY, '66.96.200.39:80');
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 0);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST,'GET');
curl_setopt ($ch, CURLOPT_HEADER, 1);
$exec = curl_exec($ch);
echo curl_error($ch);
print_r(curl_getinfo($ch));
echo $exec;

$proxy = "66.96.200.39:80";
$url = $_POST['1'];
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

?>