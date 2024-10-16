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

// SSL Verification (uncomment these lines if necessary)
// curl_setopt($verify, CURLOPT_SSL_VERIFYHOST, false);
// curl_setopt($verify, CURLOPT_SSL_VERIFYPEER, false);

// Enable verbose output (for debugging purposes)
curl_setopt($verify, CURLOPT_VERBOSE, true);

$response = curl_exec($verify);

if ($response === false) {
    echo 'Curl error: ' . curl_error($verify);
} else {
    var_dump($response); // Expecting a JSON response
}

curl_close($verify);
