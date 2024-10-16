<?php
// Connect to the database
$conn = new mysqli("localhost", "your_username", "your_password", "your_database");

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Prepare the statement
$stmt = $conn->prepare("SELECT * FROM mytable WHERE userid=? AND category=? ORDER BY id DESC");

// Bind the input parameters to the statement
$stmt->bind_param("si", $_GET['userid'], $_GET['category']);

// Execute the statement
$stmt->execute();

// Get the result set
$result = $stmt->get_result();

// Close the connection
$conn->close();
