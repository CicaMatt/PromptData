<?php
$client = new SoapClient(null, array('location' => 'http://example.com/soap', 'uri' => 'urn:example'));

// Create the acknowledgement message
$ackMessage = $client->createMessage('Ack');
$ackMessage->RqUID = 'f11958c8-3fde-42ca-bd94-94fdfca316ef';
$ackMessage->PKey = '46dba062-2105-4851-831f-a1d364741329';
$ackMessage->AppStatus->AppStatusCode = 'Accept';

// Send the acknowledgement message
$client->send($ackMessage);