<?php

class WebServiceFetcher
{
    private $url;
    private $data;
    private $obj;

    public function __construct($url)
    {
        $this->url = $url;
    }

    // Fetch data from webservice using secure HTTP request
    public function fetchData()
    {
        $this->data = file_get_contents($this->url); 

        if ($this->data === false) {
            // Handle case when the request fails
            echo 'Error: Unable to fetch data from the webservice.';
            return; 
        }

        // Validate and sanitize the input
        $this->data = trim($this->data);

        // Check if the fetched data is valid JSON
        $this->obj = json_decode($this->data);

        if (json_last_error() !== JSON_ERROR_NONE) {
            // Log the detailed error message in the server log for internal diagnostics
            $this->logError('JSON Error: ' . json_last_error_msg() . ' in response: ' . $this->data);

            // Output a generic error message for users to avoid exposing sensitive information
            echo 'Error: There was a problem processing the JSON response. Please try again later.';
        } else {
            echo 'Success: Valid JSON received!';
            // Proceed with further processing of $obj if needed
        }
    }

    // Optional: Function to log errors securely without exposing them to the user
    private function logError($message)
    {
        // Use a secure logging mechanism (e.g., to a file with appropriate permissions)
        file_put_contents('/var/log/mywebservice_errors.log', $message . PHP_EOL, FILE_APPEND);
    }
}



