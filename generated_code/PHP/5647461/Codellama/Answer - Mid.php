<?php

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "https://example.com");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, ["search" => "query"]);
$response = curl_exec($ch);
curl_close($ch);
echo $response;