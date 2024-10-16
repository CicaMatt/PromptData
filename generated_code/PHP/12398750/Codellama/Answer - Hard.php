<?php

class CSVExporter
{
    private $host;
    private $username;
    private $password;
    private $database;
    private $table;
    private $conn;
    private $filename;
    private $today;

    public function __construct($host, $username, $password, $database, $table, $today)
    {
        $this->host = $host;
        $this->username = $username;
        $this->password = $password;
        $this->database = $database;
        $this->table = $table;
        $this->today = $today;

        // Initialize filename
        $this->filename = 'ePay_' . date("Y-m-d_H-i", time()) . ".csv";

        // Connect to the MySQL database
        $this->connectToDatabase();
    }

    private function connectToDatabase()
    {
        $this->conn = mysql_connect($this->host, $this->username, $this->password) or die(mysql_error());
        mysql_select_db($this->database, $this->conn) or die(mysql_error($this->conn));
    }

    public function exportToCSV()
    {
        // Set the headers for the CSV file
        header('Content-Type: application/vnd.ms-excel');
        header("content-disposition: attachment;filename={$this->filename}");

        // Define the SQL query to retrieve data from the database
        $sql = "SELECT * FROM {$this->table} WHERE cDate='{$this->today}'";
        
        // Execute the SQL query and fetch the result
        $result = mysql_query($sql, $this->conn) or die(mysql_error($this->conn));

        if (mysql_num_rows($result) > 0) {
            // Initialize an empty string to hold the CSV data
            $fileContent = '';

            // Loop through each row of the result set and append it to the CSV data
            while ($data = mysql_fetch_array($result)) {
                // Add a new line to the CSV data for each row
                $fileContent .= "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";

                // Append the data from each column to the CSV data
                foreach ($data as $key => $value) {
                    if (is_int($key)) {
                        continue;
                    }

                    $fileContent .= $value . ',';
                }

                // Remove the trailing comma and add a new line character to the end of the row
                $fileContent = substr($fileContent, 0, -1) . "\n";
            }

            // Remove any unnecessary blank lines from the CSV data
            $fileContent = str_replace("\n\n", "\n", $fileContent);

            // Output the CSV file
            echo $fileContent;
        } else {
            // Print an error message if there is no data to export
            echo "No data found for today's date.";
        }
    }
}

// Usage
$exporter = new CSVExporter('localhost', 'root', '', 'paesana', 'ds_orders', '2008-12-21');
$exporter->exportToCSV();
?>
