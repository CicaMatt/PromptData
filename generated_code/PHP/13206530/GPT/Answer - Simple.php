<?php
function search_donar($data) {

$by_name = $data['by_name'];
$by_sex = $data['by_sex'];
$by_group = $data['by_group'];
$by_level = $data['by_level'];

$search_query = "SELECT * FROM donar WHERE 1=1";  // Add a default "true" condition

// Use a flag to keep track of whether a condition has been added
$conditions = array();

if($by_name != "") {
    $conditions[] = "name='$by_name'";
}
if($by_sex != "") {
    $conditions[] = "sex='$by_sex'";
}
if($by_group != "") {
    $conditions[] = "blood_group='$by_group'";
}
if($by_level != "") {
    $conditions[] = "e_level='$by_level'";
}

// If we have any conditions, concatenate them with AND
if (count($conditions) > 0) {
    $search_query .= " AND " . implode(" AND ", $conditions);
}

// Debugging: You can echo the query to check if it's being built correctly
// echo $search_query;

$result = mysql_query($search_query);  // This is the deprecated mysql extension, you should use mysqli or PDO

return $result;
}
