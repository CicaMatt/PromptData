<?php
$data = array(
    'secret' => "my-app-secret",
    'response' => "the-response"
);

$verify = curl_init();
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);
curl_setopt($verify, CURLOPT_SSL_VERIFYPEER, false); // For testing purposes
curl_setopt($verify, CURLOPT_SSL_VERIFYHOST, false); // For testing purposes
curl_setopt($verify, CURLOPT_VERBOSE, true); // Enable verbose output

$response = curl_exec($verify);
if ($response === false) {
    $error = curl_error($verify);
    echo "cURL Error: " . $error;
} else {
    var_dump($response);
}

curl_close($verify);
