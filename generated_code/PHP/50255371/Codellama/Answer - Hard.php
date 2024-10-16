<?php

// Load the SoapClient library
use SoapClient;

// Create a new instance of the SoapClient class with your endpoint URL
$client = new SoapClient('https://example.com/soap');

// Define the SOAP headers and body for the request
$headers = array(
    'Content-Type' => 'text/xml; charset=utf-8',
);
$body = '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
               xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <PAddRs>
      <RqUID>f11958c8-3fde-42ca-bd94-94fdfca316ef</RqUID>
      <PKey>46dba062-2105-4851-831f-a1d364741329</PKey>
      <AppStatus>
        <AppStatusCode>Accept</AppStatusCode>
      </AppStatus>
    </PAddRs>
  </soap:Body>
</soap:Envelope>';

// Send the SOAP request to the server and receive the response
$response = $client->__doRequest($body, 'http://example.com/soap', '', $headers);

// Extract the acknowledgement message from the response
preg_match('/<AppStatus>(.*)<\/AppStatus>/s', $response, $matches);
$acknowledgement = $matches[1];

// Output the acknowledgement message
echo 'Acknowledgement: ' . $acknowledgement;