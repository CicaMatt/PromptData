<?php
$hostname = "localhost";
$username = "root1"; // Intentionally incorrect username for testing
$password = "";
$database = "php_thenewboston";

class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    // Attempt to connect to the server
    $conn = mysqli_connect($hostname, $username, $password);
    if (!$conn) {
        throw new ServerException('Could not connect to server: ' . mysqli_connect_error());
    }

    // Attempt to select the database
    $conn_db = mysqli_select_db($conn, $database);
    if (!$conn_db) {
        throw new DatabaseException('Could not connect to database: ' . mysqli_error($conn));
    }

    echo "Connected.";
} catch (ServerException $ex) {
    echo "Error: " . $ex->getMessage();
} catch (DatabaseException $ex) {
    echo "Error: " . $ex->getMessage();
}
?>
