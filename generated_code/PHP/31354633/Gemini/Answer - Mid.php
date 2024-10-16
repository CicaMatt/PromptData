<?php
$data = array(
    'secret' => "my-app-secret",
    'response' => "the-response"
);

$verify = curl_init();

// Explicitly set content type for POST request
curl_setopt($verify, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));

curl_setopt($verify, CURLOPT_URL, "https://www.google.com/recaptcha/api/siteverify");
curl_setopt($verify, CURLOPT_POST, true);
curl_setopt($verify, CURLOPT_POSTFIELDS, http_build_query($data));
curl_setopt($verify, CURLOPT_RETURNTRANSFER, true);

// Essential for debugging: capture potential errors
curl_setopt($verify, CURLOPT_FAILONERROR, true); 

$response = curl_exec($verify);

if ($response === false) {
    // Handle errors gracefully, providing valuable information for troubleshooting
    $error = curl_error($verify);
    $errno = curl_errno($verify);
    echo "cURL Error ($errno): $error"; 
} else {
    // Decode JSON response for further processing
    $decodedResponse = json_decode($response);

    if ($decodedResponse === null) {
        // Handle JSON decoding errors
        echo "Error decoding JSON response.";
    } else {
        var_dump($decodedResponse); 
    }
}

curl_close($verify);
?>