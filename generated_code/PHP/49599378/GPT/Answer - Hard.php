<?php

class ElectrumConfig 
{
    private $user;
    private $pass;
    private $host;
    private $port;

    public function __construct($user, $pass, $host, $port) 
    {
        $this->user = $user;
        $this->pass = $pass;
        $this->host = $host;
        $this->port = $port;
    }

    public function getUrl() 
    {
        return 'http://' . $this->user . ':' . $this->pass . '@' . $this->host . ':' . $this->port;
    }
}

class ElectrumRequest 
{
    private $config;
    private $data;

    public function __construct(ElectrumConfig $config, $data) 
    {
        $this->config = $config;
        $this->data = $data;
    }

    public function send() 
    {
        $ch = curl_init();
        $url = $this->config->getUrl();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Content-Type: application/json',
            'Accept: application/json'
        ]);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $this->data);
        curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 10);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);

        $response = curl_exec($ch);

        if (curl_errno($ch)) {
            error_log('cURL Error: ' . curl_error($ch));
            echo 'Connection error: ' . curl_error($ch);
        } else {
            $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            if ($http_code == 200) {
                $response_data = json_decode($response, true);
                echo 'Success: ';
                print_r($response_data);
            } else {
                echo 'Error: HTTP Status Code ' . $http_code;
                echo ' Response: ' . $response;
            }
        }

        curl_close($ch);
    }
}

$config = new ElectrumConfig('user', 'pass', '127.0.0.1', '7777');

$data = json_encode([
    'id' => 'curltext',
    'method' => 'addrequest',
    'params' => [
        'amount' => '3.14',
        'memo' => 'test'
    ]
]);

$request = new ElectrumRequest($config, $data);
$request->send();

?>
