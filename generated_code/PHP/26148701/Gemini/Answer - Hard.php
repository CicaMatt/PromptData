<?php
// Initialize cURL session
$ch = curl_init();

// Set the URL you want to access
curl_setopt($ch, CURLOPT_URL, "https://maps.co.weber.ut.us/arcgis/rest/services/SDE_composite_locator/GeocodeServer/findAddressCandidates?Street=&SingleLine=3042+N+1050+W&outFields=*&outSR=102100&searchExtent=&f=json");

// Tell cURL to return the response as a string instead of outputting it directly
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

// Execute the cURL session and store the response
$response = curl_exec($ch);

// Check for cURL errors
if (curl_errno($ch)) {
    echo 'cURL Error: ' . curl_error($ch);
}

// Close the cURL session
curl_close($ch);

// Output the response
echo $response;
