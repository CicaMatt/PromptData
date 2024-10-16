<?php
function search_donar($_POST) {
    $by_name = $_POST['by_name'];
    $by_sex = $_POST['by_sex'];
    $by_group = $_POST['by_group'];
    $by_level = $_POST['by_level'];

    $search_query = "SELECT * FROM donar WHERE 1=1"; // Start with a condition that is always true

    if ($by_name != "") {
        $search_query .= " AND name='" . mysql_real_escape_string($by_name) . "'";
    }
    if ($by_sex != "") {
        $search_query .= " AND sex='" . mysql_real_escape_string($by_sex) . "'";
    }
    if ($by_group != "") {
        $search_query .= " AND blood_group='" . mysql_real_escape_string($by_group) . "'";
    }
    if ($by_level != "") {
        $search_query .= " AND e_level='" . mysql_real_escape_string($by_level) . "'";
    }

    $result = mysql_query($search_query);
  
    return $result;
}
