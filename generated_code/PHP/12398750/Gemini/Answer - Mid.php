<?php

class DatabaseConfig 
{
    const HOST = "localhost";
    const USERNAME = "root";
    const PASSWORD = "";
    const DATABASE = "paesana";
    const TABLE = "ds_orders";
}

class CSVExporter 
{
    private $conn;
    private $table;
    
    public function __construct($host, $username, $password, $database, $table) 
    {
        // Crea la connessione e gestisci eventuali errori
        $this->conn = new mysqli($host, $username, $password, $database);
        if ($this->conn->connect_error) {
            die("Connessione fallita: " . $this->conn->connect_error);
        }
        $this->table = $table;
    }

    public function export($date, $filenamePrefix) 
    {
        $csvFilename = $filenamePrefix . "_" . date("Y-m-d_H-i", time()) . ".csv";

        header("Content-Type: text/csv");
        header("Content-Disposition: attachment; filename=$csvFilename");

        // Use prepared statement 
        $sql = "SELECT * FROM " . $this->table . " WHERE cDate = ?";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $date); 

        if ($stmt->execute()) {
            $result = $stmt->get_result();
            if ($result->num_rows > 0) {
                $output = fopen("php://output", "w"); 

                fputcsv($output, ["Beneficiary Name", "Beneficiary Account No", "Beneficiary Bank Code", "Transaction Amount", "Narration"]);

                while ($data = $result->fetch_assoc()) {
                    $row = [
                        $data['customer_id'], 
                        $data['oNum'],
                        $date, 
                        $data['cShipService'] . " " . $data['cShipMethod'],
                        $data['cEmail'],
                        $data['ccType'],
                        $data['cShipInstruct'],
                        $data['cShipFname'] . " " . $data['cShipLname']
                    ];
                    fputcsv($output, $row);
                }

                fclose($output); 
            } else {
                echo "Nessun dato trovato per la data specificata.";
            }
        } else {
            echo "Errore durante l'esecuzione della query.";
        }

        $stmt->close();
        $this->conn->close();
    }
}

// Utilizzo delle classi
$exporter = new CSVExporter(
    DatabaseConfig::HOST,
    DatabaseConfig::USERNAME,
    DatabaseConfig::PASSWORD,
    DatabaseConfig::DATABASE,
    DatabaseConfig::TABLE
);

$today = "2008-12-21"; 
$exporter->export($today, "ePay");

?>
