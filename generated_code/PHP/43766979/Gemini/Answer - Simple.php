<?php
$hostname = "localhost";
$username = "root1"; // Make sure this is the correct username for your MySQL server
$password = "";
$database = "php_thenewboston"; // Double-check that this database exists

// Attempt to connect to the MySQL server
$conn = mysqli_connect($hostname, $username, $password);

// Check for connection errors and provide a more informative message
if (!$conn) {
    // The mysqli_connect_error() function gives us the specific MySQL error
    throw new ServerException('Could not connect to server. MySQL Error: ' . mysqli_connect_error());
}

// Attempt to select the database
$conn_db = mysqli_select_db($conn, $database);

// Check for database selection errors
if (!$conn_db) {
    throw new DatabaseException('Could not select database. MySQL Error: ' . mysqli_error($conn));
} else {
    echo "Connected.";
}

class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    // ... (rest of your try-catch block remains the same)
} catch (ServerException $ex) {
    echo "Error: " . $ex->getMessage();
} catch (DatabaseException $ex) {
    echo "Error: " . $ex->getMessage();
}
