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
// Enable verbose output for debugging
curl_setopt($verify, CURLOPT_VERBOSE, true); 

$response = curl_exec($verify);

if ($response === false) {
    // Handle cURL error
    $error = curl_error($verify);
    echo "cURL Error: " . $error;
} else {
    // Process the response
    var_dump($response); 
}

curl_close($verify);