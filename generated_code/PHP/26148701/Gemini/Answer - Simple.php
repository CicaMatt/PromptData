<?php
$opts = array(
    'http' => array(
        'method'  => 'GET',
        'header'  => "Content-Type: application/json\r\n",
        'ignore_errors' => true // To suppress errors, but handle them later
    )
);

$context  = stream_context_create($opts);
$response = file_get_contents("https://maps.co.weber.ut.us/arcgis/rest/services/SDE_composite_locator/GeocodeServer/findAddressCandidates?Street=&SingleLine=3042+N+1050+W&outFields=*&outSR=102100&searchExtent=&f=json", false, $context);

if ($response === false) {
    $error = error_get_last();
    echo "Error occurred: " . $error['message'];
} else {
    echo $response;
}
?>