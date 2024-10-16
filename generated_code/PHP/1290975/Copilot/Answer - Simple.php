<?php
$mysqli = new mysqli("hostname", "username", "password", "database");

// Check connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Prepare the statement
$stmt = $mysqli->prepare("SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC");

// Bind parameters
$stmt->bind_param("ss", $_GET['userid'], $_GET['category']);

// Execute the statement
$stmt->execute();

// Get the result
$result = $stmt->get_result();

// Fetch data
while ($row = $result->fetch_assoc()) {
    // Process the row
    echo "ID: " . $row['id'] . " - UserID: " . $row['userid'] . " - Category: " . $row['category'] . "<br>";
}

// Close the statement
$stmt->close();

// Close the connection
$mysqli->close();
