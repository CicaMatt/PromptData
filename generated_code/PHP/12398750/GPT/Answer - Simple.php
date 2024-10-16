<?php
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

// Connect to the database
$conn = mysql_connect($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD) or die(mysql_error());
mysql_select_db($MYSQL_DATABASE) or die(mysql_error($conn));

$filename = "ePay";
$csv_filename = $filename . "_" . date("Y-m-d_H-i", time()) . ".csv";

// Define the directory where the file should be saved
$save_directory = "/path/to/your/directory/"; // Change this to the actual directory path
$csv_file_path = $save_directory . $csv_filename;

$today = "2008-12-21";
$sql = "SELECT * FROM $MYSQL_TABLE where cDate='$today'";

$result = mysql_query($sql);

if (mysql_num_rows($result) > 0) {

    // Start building the file content
    $fileContent = "Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";
    while ($data = mysql_fetch_array($result)) {
        $fileContent .= "" . $data['customer_id'] . "," . $data['oNum'] . "," . "$today" . "," . $data['cShipService'] . " " . $data['cShipMethod'] . "," . $data['cEmail'] . "," . $data['ccType'] . "," . $data['cShipInstruct'] . "," . $data['cShipFname'] . " " . $data['cShipLname'] . "\n";
    }

    // Replace double new lines with single new lines
    $fileContent = str_replace("\n\n", "\n", $fileContent);

    // Save the content to a file
    $file = fopen($csv_file_path, "w");
    if ($file) {
        fwrite($file, $fileContent);
        fclose($file);
        echo "File saved successfully to: " . $csv_file_path;
    } else {
        echo "Failed to save the file.";
    }
} else {
    echo "No records found for the given date.";
}
?>
