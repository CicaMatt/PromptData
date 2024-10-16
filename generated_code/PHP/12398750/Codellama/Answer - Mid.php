<?php
// Connect to the database
$conn = new PDO('mysql:host=localhost;dbname=paesana', 'root', '');

// Set the query parameters
$today = "2008-12-21";
$sql = "SELECT * FROM ds_orders WHERE cDate = :today";

// Prepare the statement
$stmt = $conn->prepare($sql);

// Bind the parameter value
$stmt->bindParam(':today', $today, PDO::PARAM_STR);

// Execute the statement
$stmt->execute();

// Get the results as an array
$result = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Create the CSV file
if (count($result) > 0) {
    header("Content-Type: text/csv");
    header("Content-Disposition: attachment; filename=orders_" . date("Y-m-d_H-i") . ".csv");
    $fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";
    foreach ($result as $data) {
        $fileContent .= $data['customer_id'] . "," . $data['oNum'] . "," . "$today" . ", " . $data['cShipService'] . " " . $data['cShipMethod'] . ", " . $data['cEmail'] . ", " . $data['ccType'] . ", " . $data['cShipInstruct'] . ", " . $data['cShipFname'] . " " . $data['cShipLname'] . "\n";
    }
    echo $fileContent;
}
?>