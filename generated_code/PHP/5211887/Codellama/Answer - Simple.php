<?php

$proxy = array('66.96.200.39', '80');
$url = $_POST['1'];

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_PROXY, $proxy[0]);
curl_setopt($ch, CURLOPT_PROXYPORT, $proxy[1]);
curl_setopt($ch, CURLOPT_HEADER, 1);

$exec = curl_exec($ch);
if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
} else {
    $curl_info = curl_getinfo($ch);
    echo '<br />';
    print_r($curl_info);
    curl_close($ch);
}