<?php 
$context = stream_context_create(array('ssl' => array('verify_peer' => false)));
$response = file_get_contents("https://maps.co.weber.ut.us/arcgis/rest/services/SDE_composite_locator/GeocodeServer/findAddressCandidates?Street=&SingleLine=3042+N+1050+W&outFields=*&outSR=102100&searchExtent=&f=json", false, $context);
echo $response; 
?>