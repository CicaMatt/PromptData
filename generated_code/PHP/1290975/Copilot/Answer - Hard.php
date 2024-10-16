<?php
// Database connection
$mysqli = new mysqli("localhost", "username", "password", "database");

// Check connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Retrieve parameters using filter_input to avoid direct access to $_GET
$userid = filter_input(INPUT_GET, 'userid', FILTER_SANITIZE_STRING);
$category = filter_input(INPUT_GET, 'category', FILTER_SANITIZE_STRING);

// Check if parameters are valid
if ($userid === null || $category === null) {
    die("Invalid input parameters.");
}

// Prepare the statement
$stmt = $mysqli->prepare("SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC");

// Bind parameters
$stmt->bind_param("ss", $userid, $category);

// Execute the statement
$stmt->execute();

// Get the result
$result = $stmt->get_result();

// Fetch data
while ($row = $result->fetch_assoc()) {
    // Process the row
    echo "ID: " . $row["id"] . " - UserID: " . $row["userid"] . " - Category: " . $row["category"] . "<br>";
}

// Close the statement and connection
$stmt->close();
$mysqli->close();
?>
