<?php
// Define the SOAP server
$server = new SoapServer(null, array('uri' => "http://example.com/soap"));

// Define the function to handle the request
function handleRequest($request) {
    // Send the acknowledgement
    sendAcknowledgement();

    // Process the actual request (this can be done asynchronously)
    processRequest($request);
}

// Function to send the acknowledgement
function sendAcknowledgement() {
    $ack = <<<ACK
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
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
</soap:Envelope>
ACK;

    // Output the acknowledgement
    echo $ack;
    // Flush the output buffer to ensure the acknowledgement is sent immediately
    ob_flush();
    flush();
}

// Function to process the actual request
function processRequest($request) {
    // Your request processing logic here
}

// Register the function with the SOAP server
$server->addFunction("handleRequest");

// Handle the SOAP request
$server->handle();
?>
