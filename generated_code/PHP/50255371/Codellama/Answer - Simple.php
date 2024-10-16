<?php
$server = new SoapServer(null, array('uri' => 'urn:my-service', 'style' => SOAP_RPC, 'use' => SOAP_ENCODED));
$server->setClass("MySoapService", $wsdl);
$server->addFunction("submitInv");
$server->handle();

// Send acknowledgement message before responding to the request
$acknowledgement = new stdClass();
$acknowledgement->RqUID = "f11958c8-3fde-42ca-bd94-94fdfca316ef";
$acknowledgement->PKey = "46dba062-2105-4851-831f-a1d364741329";
$acknowledgement->AppStatusCode = "Accept";
echo $server->__soapCall("PAddRs", array($acknowledgement), null, SOAP_SINGLE_ELEMENT_ARRAYS);