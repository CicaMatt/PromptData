<?php
$hostname = "localhost";
$username = "root1";
$password = "";
$database = "php_thenewboston";

// Custom exception classes
class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    // Establishing connection to the server
    $conn = mysqli_connect($hostname, $username, $password);
    if (!$conn) {
        throw new ServerException('Could not connect to server: ' . mysqli_connect_error());
    }

    // Selecting the database
    $conn_db = mysqli_select_db($conn, $database);
    if (!$conn_db) {
        throw new DatabaseException('Could not connect to database: ' . mysqli_error($conn));
    }

    echo "Connected successfully.";
} catch (ServerException $ex) {
    echo "Error: " . $ex->getMessage();
} catch (DatabaseException $ex) {
    echo "Error: " . $ex->getMessage();
} finally {
    // Close the connection if it was established
    if ($conn) {
        mysqli_close($conn);
    }
}
?>
