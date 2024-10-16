<?php
$today = "2008-12-21";
$sql = "SELECT * FROM $MYSQL_TABLE where cDate='$today'";
$result = mysql_query($sql);
if(mysql_num_rows($result)>0){
    $fileContent="Beneficiary Name,Beneficiary Account No,Beneficiary Bank Code,Transaction Amount,Narration\n";
    while($data=mysql_fetch_array($result)) {
        $fileContent.= "".$data['customer_id'].",".$data['oNum'].","."$today".",".$data['cShipService']." ".$data['cShipMethod'].",".$data['cEmail'].",".$data['ccType'].",".$data['cShipInstruct'].",".$data['cShipFname']." ".$data['cShipLname']."\n";
    }
    $fileContent=str_replace("

","
",$fileContent);
    // Modify the echo statement to save the file to disk
    file_put_contents($csv_filename, $fileContent);
}