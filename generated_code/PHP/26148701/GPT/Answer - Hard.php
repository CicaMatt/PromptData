<?php
// Define the context options to disable SSL certificate verification
$contextOptions = array(
    "ssl" => array(
        "verify_peer" => false,
        "verify_peer_name" => false,
        "allow_self_signed" => true
    )
);

// Create a stream context with the specified options
$context = stream_context_create($contextOptions);

// Fetch the contents of the URL with the custom context
$response = file_get_contents("https://maps.co.weber.ut.us/arcgis/rest/services/SDE_composite_locator/GeocodeServer/findAddressCandidates?Street=&SingleLine=3042+N+1050+W&outFields=*&outSR=102100&searchExtent=&f=json", false, $context);

// Output the response
echo $response;
?>
