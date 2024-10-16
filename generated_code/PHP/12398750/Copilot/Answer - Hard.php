<?php
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

// Create a connection
$conn = new mysqli($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $MYSQL_DATABASE);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$filename = "ePay";
$csv_filename = $filename . "_" . date("Y-m-d_H-i", time()) . ".csv";
$directory = "/path/to/your/directory/"; // Change this to your desired directory path

$today = "2008-12-21";
$sql = "SELECT * FROM $MYSQL_TABLE WHERE cDate = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $today);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";
    while ($data = $result->fetch_assoc()) {
        $fileContent .= $data['customer_id'] . "," . $data['oNum'] . "," . $today . "," . $data['cShipService'] . " " . $data['cShipMethod'] . "," . $data['cEmail'] . "," . $data['ccType'] . "," . $data['cShipInstruct'] . "," . $data['cShipFname'] . " " . $data['cShipLname'] . "\n";
    }

    $fileContent = str_replace("\n\n", "\n", $fileContent);

    // Save the file to the specified directory
    file_put_contents($directory . $csv_filename, $fileContent);
    echo "File saved successfully to " . $directory . $csv_filename;
} else {
    echo "No records found for the specified date.";
}

$stmt->close();
$conn->close();
?>
