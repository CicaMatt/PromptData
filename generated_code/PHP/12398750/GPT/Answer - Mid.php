<?php
// Database connection details
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

// Create a new MySQLi connection
$conn = new mysqli($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $MYSQL_DATABASE);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Generate the filename with the current timestamp
$filename = "ePay";
$csv_filename = $filename . "_" . date("Y-m-d_H-i", time()) . ".csv";

// Define the directory where the file will be saved (ensure this directory is writable)
$save_directory = '/path/to/your/directory/'; // Adjust this path to a writable directory
$csv_filepath = $save_directory . $csv_filename;

// The date for filtering the data
$today = "2008-12-21";

// SQL query with prepared statements to prevent SQL injection
$stmt = $conn->prepare("SELECT customer_id, oNum, cShipService, cShipMethod, cEmail, ccType, cShipInstruct, cShipFname, cShipLname FROM $MYSQL_TABLE WHERE cDate = ?");
$stmt->bind_param('s', $today);
$stmt->execute();

// Get the result set from the query
$result = $stmt->get_result();

// Check if there are any records
if ($result->num_rows > 0) {

    // Open a file handle for writing
    if ($file = fopen($csv_filepath, 'w')) {

        // Write the CSV header row
        $fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";
        fwrite($file, $fileContent);

        // Fetch each row and write to the CSV file
        while ($data = $result->fetch_assoc()) {
            $fileContent = $data['customer_id'] . "," . 
                           $data['oNum'] . "," . 
                           "$today" . "," . 
                           $data['cShipService'] . " " . $data['cShipMethod'] . "," . 
                           $data['cEmail'] . "," . 
                           $data['ccType'] . "," . 
                           $data['cShipInstruct'] . "," . 
                           $data['cShipFname'] . " " . $data['cShipLname'] . "\n";
            fwrite($file, $fileContent);
        }

        // Close the file handle
        fclose($file);

        echo "CSV file saved successfully to " . $csv_filepath;
    } else {
        echo "Error: Unable to write to the specified directory.";
    }
} else {
    echo "No data found for the specified date.";
}

// Close the statement and connection
$stmt->close();
$conn->close();
?>
