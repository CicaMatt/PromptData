<?php
// Establish a database connection (replace with your actual credentials)
$servername = "your_servername";
$username = "your_username";
$password = "your_password";
$dbname = "your_dbname";

$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Sanitize and validate input (crucial even with prepared statements)
$userid = $conn->real_escape_string($_GET['userid']);
$category = $conn->real_escape_string($_GET['category']);

// Prepare the SQL statement
$stmt = $conn->prepare("SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC");

// Bind parameters
$stmt->bind_param("ss", $userid, $category); 

// Execute the statement
$stmt->execute();

// Get the result set
$result = $stmt->get_result();

// Fetch and process the data
while ($row = $result->fetch_assoc()) {
    // Access data using $row['column_name']
    // ... your data processing logic here
}

// Close the statement and connection
$stmt->close();
$conn->close();
