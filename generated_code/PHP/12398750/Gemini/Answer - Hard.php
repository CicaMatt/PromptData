<?php
class DatabaseConnectionException extends Exception 
{

}
class CSVExportException extends Exception 
{

}

class DatabaseExporter 
{
    private $dbHost;
    private $dbUsername;
    private $dbPassword;
    private $dbName;
    private $table;
    private $conn;
    private $filename;
    private $csvFilepath;

    public function __construct() 
    {
        $this->dbHost = "localhost";
        $this->dbUsername = "root";
        $this->dbPassword = "";
        $this->dbName = "paesana";
        $this->table = "ds_orders";
        $this->filename = "ePay_" . date("Y-m-d_H-i", time()) . ".csv";
        $this->csvFilepath = '/path/to/save/csv/files/' . $this->filename;

        $this->connectDatabase();
    }

    private function connectDatabase() 
    {
        $this->conn = new mysqli($this->dbHost, $this->dbUsername, $this->dbPassword, $this->dbName);

        if ($this->conn->connect_error) {
            throw new DatabaseConnectionException("Connection failed: " . $this->conn->connect_error);
        }
    }

    public function exportCSV($date) 
    {
        // Prepara una query parametrizzata per prevenire SQL injection
        $sql = "SELECT * FROM $this->table WHERE cDate = ?";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $date);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($file = fopen($this->csvFilepath, "w")) {
            $header = [
                "Beneficiary Name", 
                "Beneficiary Account No", 
                "Beneficiary Bank Code", 
                "Transaction Amount", 
                "Narration"
            ];
            fputcsv($file, $header);

            if ($result->num_rows > 0) {
                while ($data = $result->fetch_assoc()) {
                    $csv_row = [
                        $data['customer_id'],
                        $data['oNum'],
                        $date,
                        $data['cShipService'] . " " . $data['cShipMethod'],
                        $data['cEmail'],
                        $data['ccType'],
                        $data['cShipInstruct'],
                        $data['cShipFname'] . " " . $data['cShipLname']
                    ];
                    fputcsv($file, $csv_row);
                }
            } else {
                echo "No records found for the specified date.";
            }

            fclose($file);
            echo "<p>CSV file created successfully. <a href=\"{$this->csvFilepath}\" download>Download</a></p>";
        } else {
            throw new CSVExportException("Error: Unable to open file for writing.");
        }

        $stmt->close();
    }

    public function closeConnection() 
    {
        $this->conn->close();
    }
}

class Main
{
    public function execute() 
    {
        try {
            $exporter = new DatabaseExporter();
            $exporter->exportCSV("2008-12-21"); 
            $exporter->closeConnection();
        } catch (DatabaseConnectionException $e) {
            echo "Error: " . $e->getMessage();
        } catch (CSVExportException $e) {
            echo "Error: " . $e->getMessage();
        }
    }
}
