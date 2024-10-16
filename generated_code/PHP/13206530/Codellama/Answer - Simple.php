<?php
$search_query = "SELECT * FROM donar";
if($by_name !="") {
  $search_query .= " WHERE name='$by_name'";
}
if($by_sex !="") {
  $search_query .= " AND sex='$by_sex'";
}
if($by_group !="") {
  $search_query .= " AND blood_group='$by_group'";
}
if($by_level !="") {
  $search_query .= " AND e_level='$by_level'";
}