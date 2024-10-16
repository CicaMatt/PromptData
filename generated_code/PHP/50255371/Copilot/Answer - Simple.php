<?php
// Start output buffering
ob_start();

// Capture the incoming SOAP request
$request = file_get_contents('php://input');

// Send the acknowledgement message
$ackMessage = <<<ACK
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
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

echo $ackMessage;

// Flush the buffer to send the acknowledgement immediately
ob_flush();
flush();

// Continue processing the actual request
// Your processing logic here

?>
