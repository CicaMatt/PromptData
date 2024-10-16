<?php

// ElectrumConfig class to store Electrum configuration
class ElectrumConfig 
{
    private $user;
    private $pass;
    private $host;
    private $port;

    public function __construct() 
    {
        // Load Electrum credentials from environment variables
        $this->user = getenv('ELECTRUM_USER');
        $this->pass = getenv('ELECTRUM_PASS');
        $this->host = '127.0.0.1';
        $this->port = 7777;
    }

    public function getUrl() 
    {
        // Build and return the Electrum URL
        return "http://{$this->user}:{$this->pass}@{$this->host}:{$this->port}";
    }
}

// ElectrumClient class to handle cURL requests
class ElectrumClient 
{
    private $config;

    public function __construct(ElectrumConfig $config) 
    {
        $this->config = $config;
    }

    public function sendRequest($method, $params) 
    {
        $url = $this->config->getUrl();

        // Prepare data to send in the JSON body
        $data = json_encode([
            'id' => 'curltext',
            'method' => $method,
            'params' => $params
        ]);

        // Initialize cURL session
        $ch = curl_init($url);

        // Set cURL options
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

        // Execute cURL request and fetch response
        $response = curl_exec($ch);

        // Check for cURL errors
        if ($response === false) {
            $error = curl_error($ch);
            // Log or handle the error securely without exposing sensitive information
            error_log("cURL error: $error");
            echo "An error occurred while connecting to the Electrum server. Please try again later.";
        } else {
            // Process the response
            $http_status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            if ($http_status == 200) {
                echo "Connection successful: " . htmlspecialchars($response);
            } else {
                echo "Failed to connect. HTTP Status: " . $http_status;
            }
        }

        // Close cURL session
        curl_close($ch);
    }
}

// Main execution flow
$config = new ElectrumConfig();
$client = new ElectrumClient($config);

// Define parameters to send in the request
$params = [
    'amount' => '3.14',
    'memo' => 'test'
];

// Send request to Electrum server
$client->sendRequest('addrequest', $params);

?>
