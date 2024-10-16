<?php
class SoapServerHandler 
{
    private $server;

    public function __construct($wsdl) 
    {
        // Crea l'istanza di SoapServer all'interno della classe
        $this->server = new SoapServer($wsdl);
        $this->server->setClass("MySoapService");
    }

    public function handle() 
    {
        // Gestisce le richieste SOAP
        $this->server->handle();
    }
}



class MySoapService 
{
    public function initialize() 
    {
        // Inizializzazione del server
        $soapHandler = new SoapServerHandler("path/to/wsdl.wsdl");
        $soapHandler->handle();
    }


    public function submitInb($request) 
    {
        // Invio immediato della conferma di ricezione
        $this->sendAcknowledgement($request->RqUID); 

        // Simulazione di elaborazione della richiesta (sostituisci con la tua logica effettiva)
        sleep(2); 

        // Prepara la risposta effettiva
        $response = new stdClass();
        $response->RqUID = $request->RqUID;
        $response->Status = "Processed";
        return $response;
    }

    private function sendAcknowledgement($requestUID) 
    {
        $ackMessage = <<<XML
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <Acknowledgement>
            <RqUID>$requestUID</RqUID>
            <Status>Received</Status>
        </Acknowledgement>
    </soap:Body>
</soap:Envelope>
XML;

        // Ottieni i dati POST grezzi (la richiesta SOAP originale)
        $rawData = file_get_contents('php://input');

        // Estrai l'intestazione 'SOAPAction' dai dati grezzi
        preg_match('/SOAPAction: "(.*?)"/', $rawData, $matches);
        $soapAction = $matches[1];

        // Prepara le intestazioni per la risposta di conferma
        $headers = [
            'Content-Type: text/xml; charset=utf-8',
            'SOAPAction: "' . $soapAction . '"', 
        ];

        // Invia la conferma direttamente al buffer di output
        header("Connection: close"); 
        foreach ($headers as $header) {
            header($header);
        }
        echo $ackMessage;

        // Importante: svuota il buffer di output e chiudi la connessione
        ob_end_flush();
        flush();
        // Se si utilizza FastCGI (comune in hosting condivisi)
        fastcgi_finish_request(); 
    }
}
