<?php
// Check if the form is submitted and the URL is present in $_POST
if (isset($_POST['url'])) {
    $url = $_POST['url']; // Get the URL from the POST request

    // Initialize cURL
    $ch = curl_init();

    // Set cURL options
    curl_setopt($ch, CURLOPT_URL, $url);  // Set the target URL
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);  // Return the result as a string
    curl_setopt($ch, CURLOPT_PROXY, '66.96.200.39:80');  // Set the proxy server
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);  // Follow any "Location" header in the response
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');  // Use the GET method
    curl_setopt($ch, CURLOPT_HEADER, 1);  // Include the headers in the output

    // Execute the cURL request
    $response = curl_exec($ch);

    // Check if any error occurred
    if (curl_errno($ch)) {
        echo 'cURL error: ' . curl_error($ch);
    } else {
        // Print detailed information about the request
        $curl_info = curl_getinfo($ch);
        print_r($curl_info);
        echo $response;  // Output the result
    }

    // Close the cURL session
    curl_close($ch);
} else {
    echo "No URL provided.";
}
?>
