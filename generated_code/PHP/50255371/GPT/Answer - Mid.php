<?php

// Set the content type to SOAP XML
header("Content-Type: text/xml; charset=utf-8");

// Capture the SOAP request
$rawPost = file_get_contents('php://input');

// Extract the request type (SubmitInv or RequestInv) to determine the acknowledgment response
$requestType = ''; // You would parse $rawPost to determine the type (SubmitInv or RequestInv)

// Generate and send the acknowledgment response
$acknowledgmentResponse = generateAcknowledgmentResponse($requestType);

// Output acknowledgment response and flush the buffer to send it immediately
echo $acknowledgmentResponse;
ob_flush(); // Ensure the response is sent immediately
flush(); // Forces PHP to send the acknowledgment to the client

// Continue processing the actual request (this is where the real processing happens)
$finalResponse = processRequest($rawPost);

// After processing the actual request, send the real SOAP response
echo $finalResponse;

/**
 * Function to generate acknowledgment SOAP message
 */
function generateAcknowledgmentResponse($requestType) {
    $rqUid = uniqid();  // You can use the actual Request UID from the $rawPost if needed
    $appStatusCode = 'Accept'; // Depending on your logic, this might change

    // Example SOAP acknowledgment message
    return <<<XML
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
               xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <PAddRs>
      <RqUID>$rqUid</RqUID>
      <PKey>46dba062-2105-4851-831f-a1d364741329</PKey>
      <AppStatus>
        <AppStatusCode>$appStatusCode</AppStatusCode>
      </AppStatus>
    </PAddRs>
  </soap:Body>
</soap:Envelope>
XML;
}

/**
 * Function to process the original SOAP request and return the actual response
 */
function processRequest($rawPost) {
    // Add your logic here to handle the actual SOAP request

    // After processing, construct the final SOAP response (as needed by your system)
    return <<<XML
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
               xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <Response>
      <RqUID>final-response-uid</RqUID>
      <ResponseStatus>Success</ResponseStatus>
    </Response>
  </soap:Body>
</soap:Envelope>
XML;
}
