<?php

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "https://example.com/search");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, ["q" => "query"]);
$response = curl_exec($ch);