<?php
$data = array(
    // Replace with your actual secret key and response token
    'secret' => 'your-secret-key', 
    'response' => 'the-response' 
);

$verify = curl_init();
curl_setopt($verify, CURLOPT_URL, 'https://www.google.com/recaptcha/api/siteverify');
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($verify);

if ($response === false) {
    $error = curl_error($verify);
    curl_close($verify);
    die('Curl error: ' . $error);
}

curl_close($verify);

$responseData = json_decode($response, true);

if ($responseData['success']) {
    echo 'Verification successful!';
} else {
    echo 'Verification failed: ' . $responseData['error-codes'][0];
}
?>
