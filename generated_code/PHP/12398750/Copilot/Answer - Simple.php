<?php
$MYSQL_HOST = "localhost";
$MYSQL_USERNAME = "root";
$MYSQL_PASSWORD = "";
$MYSQL_DATABASE = "paesana";
$MYSQL_TABLE = "ds_orders";

mysql_connect($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD) or die(mysql_error());
mysql_select_db($MYSQL_DATABASE) or die(mysql_error());

$filename = "ePay";
$csv_filename = $filename . "_" . date("Y-m-d_H-i", time()) . ".csv";
$filepath = "/path/to/your/directory/" . $csv_filename; // Change this to your desired directory

$today = "2008-12-21";
$sql = "SELECT * FROM $MYSQL_TABLE WHERE cDate='$today'";

$result = mysql_query($sql);

if (mysql_num_rows($result) > 0) {
    $file = fopen($filepath, 'w');

    // Add the header of the CSV file
    $header = ["Beneficiary Name", "Beneficiary Account No", "Beneficiary Bank Code", "Transaction Amount", "Narration"];
    fputcsv($file, $header);

    // Add the data rows
    while ($data = mysql_fetch_array($result)) {
        $row = [
            $data['customer_id'],
            $data['oNum'],
            $today,
            $data['cShipService'] . " " . $data['cShipMethod'],
            $data['cEmail'],
            $data['ccType'],
            $data['cShipInstruct'],
            $data['cShipFname'] . " " . $data['cShipLname']
        ];
        fputcsv($file, $row);
    }

    fclose($file);
    echo "CSV file has been saved to " . $filepath;
} else {
    echo "No data found for the specified date.";
}
?>
