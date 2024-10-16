<?php
$url = "https://www.example.com";
$method = "POST";
$headers = array("Content-Type: application/x-www-form-urlencoded");

$data = http_build_query(array("parameter1" => "value1", "parameter2" => "value2"));
$options = array(
    'http' => array(
        'method' => $method,
        'header' => implode("\r\n", $headers),
        'content' => $data
    )
);

$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);

echo $result;