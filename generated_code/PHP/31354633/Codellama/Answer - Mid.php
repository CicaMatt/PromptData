<?php
$data = array(
    'secret' => "my-app-secret",
    'response' => "the-response"
);

$verify = curl_init();
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_VERBOSE, true); // <-- Add this line to enable verbose mode
$response = curl_exec($verify);

var_dump($response);