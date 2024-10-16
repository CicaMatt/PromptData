<?php
// Establish a secure MySQLi connection (replace with your actual credentials)
$mysqli = new mysqli("your_host", "your_username", "your_password", "your_database");

// Check for connection errors
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Prepare the SQL statement with placeholders
$stmt = $mysqli->prepare("SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC");

// Check for preparation errors
if (!$stmt) {
    die("Prepare failed: " . $mysqli->error);
}

// Bind parameters to the placeholders
$stmt->bind_param("ss", $_GET['userid'], $_GET['category']);

// Execute the prepared statement
$stmt->execute();

// Get the result set
$result = $stmt->get_result();

// Fetch and process the data
while ($row = $result->fetch_assoc()) {
    // Process the retrieved data as needed
    // ...
}

// Close the prepared statement and the database connection
$stmt->close();
$mysqli->close();
