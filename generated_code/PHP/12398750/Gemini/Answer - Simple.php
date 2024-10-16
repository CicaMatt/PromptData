<?php
// Database connection details (consider moving these to a separate config file)
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

// Establish a database connection (using mysqli, the improved MySQL extension)
$conn = mysqli_connect($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $MYSQL_DATABASE);

// Check for connection errors
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// CSV file setup
$filename = "ePay";
$csv_filename = $filename . "_" . date("Y-m-d_H-i", time()) . ".csv";
$csv_filepath = "/path/to/your/directory/" . $csv_filename; // Specify the full path to save the file

// Query the database (ensure $today is set correctly if you need specific data)
$today = "2008-12-21"; // Or use $today = date("Y-m-d"); for today's date
$sql = "SELECT * FROM $MYSQL_TABLE WHERE cDate = '$today'";
$result = mysqli_query($conn, $sql);

// Check for query errors
if (!$result) {
    die("Query failed: " . mysqli_error($conn));
}

// Generate CSV content
$fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";

if (mysqli_num_rows($result) > 0) {
    while ($data = mysqli_fetch_assoc($result)) { // Use mysqli_fetch_assoc for associative array
        // Adjust the CSV columns based on your actual database fields
        $fileContent .= 
            $data['customer_id'] . "," . 
            $data['oNum'] . "," .
            $today . "," . 
            $data['cShipService'] . " " . $data['cShipMethod'] . "," .
            $data['cEmail'] . "," . 
            $data['ccType'] . "," . 
            $data['cShipFname'] . " " . $data['cShipLname'] . "\n"; // Fixed the typo here
    }
}

// Write the CSV content to the file
if (file_put_contents($csv_filepath, $fileContent) !== false) {
    // Optionally, provide feedback to the user
    echo "CSV file '$csv_filename' has been created and saved successfully.";
} else {
    echo "Failed to create the CSV file.";
}

// Close the database connection
mysqli_close($conn);
?>
