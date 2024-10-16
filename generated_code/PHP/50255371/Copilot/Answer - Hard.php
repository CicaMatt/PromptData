<?php
// Step 1: Create a SOAP Server
class MySoapServer {
    public function SubmitInv($request) {
        // Step 2: Send Acknowledgment
        $this->sendAcknowledgment($request->RqUID);

        // Step 3: Process the Request
        // Simulate long processing time
        sleep(5);

        // Return the actual response
        return $this->createResponse($request);
    }

    private function sendAcknowledgment($rqUID) {
        $ack = <<<ACK
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
               xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <soap:Body>
    <PAddRs>
      <RqUID>$rqUID</RqUID>
      <AppStatus>
        <AppStatusCode>Accept</AppStatusCode>
      </AppStatus>
    </PAddRs>
  </soap:Body>
</soap:Envelope>
ACK;
        // Send the acknowledgment
        echo $ack;
        flush();
        ob_flush();
    }

    private function createResponse($request) {
        // Create the actual response
        return [
            'RqUID' => $request->RqUID,
            'PKey' => $request->PKey,
            'AppStatus' => [
                'AppStatusCode' => 'Processed'
            ]
        ];
    }
}

// Initialize SOAP Server
$server = new SoapServer(null, ['uri' => 'http://example.com/soap']);
$server->setClass('MySoapServer');
$server->handle();
?>
