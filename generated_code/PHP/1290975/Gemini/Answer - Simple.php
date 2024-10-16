<?php
// Database connection (replace with your credentials)
$conn = new mysqli('your_hostname', 'your_username', 'your_password', 'your_database');

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Prepare the statement
$stmt = $conn->prepare("SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC");

// Bind parameters
$stmt->bind_param("ss", $_GET['userid'], $_GET['category']);

// Execute the statement
$stmt->execute();

// Get the result set
$result = $stmt->get_result();

// Fetch data (adjust as needed)
while ($row = $result->fetch_assoc()) {
    // Process the row data
}

// Close the statement and connection
$stmt->close();
$conn->close();
?>