<?php
// Database credentials
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

// Set up a connection using mysqli (improved over mysql_* functions)
$mysqli = new mysqli($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $MYSQL_DATABASE);

// Check connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Secure the input date (ensure it's from a trusted source)
$today = "2008-12-21";  // Example: hard-coded for now, should use proper input sanitization

// Prepare the query securely with parameterized statements
$sql = "SELECT * FROM $MYSQL_TABLE WHERE cDate = ?";
$stmt = $mysqli->prepare($sql);
$stmt->bind_param("s", $today);

// Execute the query
$stmt->execute();
$result = $stmt->get_result();

// Check if there are results
if ($result->num_rows > 0) {
    // Define the filename and directory where the CSV will be saved
    $filename = "ePay_" . date("Y-m-d_H-i", time()) . ".csv";
    $file_path = "/path/to/your/directory/" . $filename;  // Ensure this directory is writable by the web server

    // Open the file for writing
    if ($file = fopen($file_path, "w")) {
        // Write the CSV headers
        $fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";

        // Loop through the result and write each row to the CSV
        while ($data = $result->fetch_assoc()) {
            $fileContent .= "{$data['customer_id']},{$data['oNum']},{$today},{$data['cShipService']} {$data['cShipMethod']},{$data['cEmail']},{$data['ccType']},{$data['cShipInstruct']},{$data['cShipFname']} {$data['cShipLname']}\n";
        }

        // Write the file content to the file
        fwrite($file, $fileContent);

        // Close the file
        fclose($file);

        echo "CSV file saved successfully at: " . $file_path;
    } else {
        echo "Error: Unable to write to file.";
    }
} else {
    echo "No records found for the specified date.";
}

// Close the statement and connection
$stmt->close();
$mysqli->close();
?>
