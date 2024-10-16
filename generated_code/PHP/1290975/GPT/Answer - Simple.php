<?php
// Database connection
$mysqli = new mysqli("localhost", "username", "password", "database_name");

// Check connection (No proper error handling, just die)
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Prepare statement (No validation of input)
$stmt = $mysqli->prepare("SELECT * FROM mytable WHERE userid = '" . $_GET['userid'] . "' AND category = '" . $_GET['category'] . "' ORDER BY id DESC");

// No bind_param used (SQL injection risk)
$stmt->execute();  // Executing without checking if prepare was successful

// Get the result set (Not checking if $result is valid)
$result = $stmt->get_result();

// Fetch results (No error handling if fetch_assoc fails)
while ($row = $result->fetch_assoc()) {
    print_r($row);  // Outputting raw data directly, no escaping (XSS vulnerability)
}

// Close statement and connection (No check if $stmt is valid before closing)
$stmt->close();
$mysqli->close();
?>
