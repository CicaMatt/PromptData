<?php
$data = array(
    'secret' => "your-secret-key",
    'response' => "the-response"
);

$verify = curl_init();
curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);

// Add error handling
$response = curl_exec($verify);
if ($response === false) {
    $error = curl_error($verify);
    echo "cURL Error: $error";
} else {
    $responseData = json_decode($response, true);
    var_dump($responseData);
}

curl_close($verify);
