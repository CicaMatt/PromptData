<?php

// A function to handle the SOAP request and send an immediate ACK
function processSoapRequest($request) {
    // Extract necessary data from the request, like RqUID
    $rqUID = extractRqUIDFromRequest($request);

    // Send the acknowledgment response immediately
    sendAcknowledgementResponse($rqUID);

    // Continue processing the actual SOAP request (e.g., business logic here)
    // ... (processing logic)

    // After processing, send the final response back
    $finalResponse = generateFinalResponse($request);
    echo $finalResponse;  // Send the final response
}

// A function to send the acknowledgment SOAP response
function sendAcknowledgementResponse($rqUID) {
    // Generate the acknowledgement message
    $ackMessage = createAckMessage($rqUID);

    // Flush the acknowledgement message to the client
    // Ensure we don't keep the connection open for the next part
    ignore_user_abort(true);  // Ignore if the user aborts the connection
    ob_end_clean();           // Clear the output buffer if there's anything

    // Send custom headers to indicate the response is partial (or custom)
    header('Content-Type: text/xml; charset=utf-8');
    header('Content-Length: ' . strlen($ackMessage));
    header('Connection: close');

    // Send the ACK response
    echo $ackMessage;

    // Flush and close the connection for the ACK
    flush();
    if (function_exists('fastcgi_finish_request')) {
        fastcgi_finish_request();  // Necessary for FastCGI (common in PHP-FPM)
    }
}

// A function to create the acknowledgment message
function createAckMessage($rqUID) {
    // Build the acknowledgment SOAP message based on the example you provided
    return '<?xml version="1.0" encoding="UTF-8"?>' .
           '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">' .
           '<soap:Body>' .
           '<PAddRs>' .
           '<RqUID>' . htmlspecialchars($rqUID) . '</RqUID>' .
           '<AppStatus>' .
           '<AppStatusCode>Accept</AppStatusCode>' .
           '</AppStatus>' .
           '</PAddRs>' .
           '</soap:Body>' .
           '</soap:Envelope>';
}

// A function to extract RqUID from the SOAP request
function extractRqUIDFromRequest($request) {
    // This is a simplified example. In reality, you'd use a proper XML parser.
    $matches = [];
    preg_match('/<RqUID>([^<]+)<\/RqUID>/', $request, $matches);
    return $matches[1] ?? 'unknown';
}

// A function to generate the final SOAP response after processing
function generateFinalResponse($request) {
    // Do your business logic here and generate the appropriate SOAP response
    return '<?xml version="1.0" encoding="UTF-8"?>' .
           '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">' .
           '<soap:Body>' .
           '<FinalResponse>' .
           '<Status>Success</Status>' .
           '</FinalResponse>' .
           '</soap:Body>' .
           '</soap:Envelope>';
}

// Simulate receiving a SOAP request
$request = file_get_contents('php://input');

// Process the SOAP request and manage the flow of ACK and final response
processSoapRequest($request);

?>
