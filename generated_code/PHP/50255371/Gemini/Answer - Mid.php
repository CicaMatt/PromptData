<?php
// Custom SOAP Server (or extension)
class MySoapServer extends SoapServer {

    public function handle($request = null) {
        // 1. Intercept and parse the request
        $soapRequest = $request ?: file_get_contents('php://input');
        // Parse the SOAP request (implementation depends on your SOAP library)

        // 2. Generate the acknowledgement
        $acknowledgementXml = '<soap:Envelope ...>
            <soap:Body>
                <Acknowledgement>
                    <RqUID>...</RqUID> 
                    <Status>Received</Status> 
                </Acknowledgement>
            </soap:Body>
        </soap:Envelope>';

        // 3. Send the acknowledgement
        ob_start();
        header('Content-Type: text/xml'); 
        echo $acknowledgementXml;
        ob_flush(); 
        flush(); // Force sending to the client

        // 4. Process the request
        $responseXml = $this->processRequest($soapRequest); 

        // 5. Send the actual response
        ob_end_flush();
        echo $responseXml;
    }

    // ... other methods
}