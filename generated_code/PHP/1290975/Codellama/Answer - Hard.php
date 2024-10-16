<?php
// Connect to the database
$conn = new mysqli('localhost', 'user', 'password', 'mydatabase');

// Prepare the SQL statement with placeholder values for the user ID and category
$stmt = $conn->prepare("SELECT * FROM mytable WHERE userid=? AND category=? ORDER BY id DESC");

// Bind the input from the URL parameters to the placeholders in the SQL statement
$stmt->bind_param('ss', $_GET['userid'], $_GET['category']);

// Execute the prepared statement and get the result set
$result = $stmt->execute();
$rows = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);

// Free up resources used by the prepared statement
$stmt->close();

// Close the database connection
$conn->close();