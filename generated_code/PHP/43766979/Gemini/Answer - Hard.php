<?php
$hostname = "localhost";
$username = "root1"; 
$password = "";
$database = "php_thenewboston"; 

// Establish the initial connection, but don't select the database yet
$conn = mysqli_connect($hostname, $username, $password);

class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    if (!$conn) {
        // Check for specific connection errors
        if (mysqli_connect_errno() == 1045) { 
            throw new ServerException('Access denied. Please check your username and password.');
        } else {
            throw new ServerException('Could not connect to the server. ' . mysqli_connect_error());
        }
    } else {
        // Now, attempt to select the database
        $conn_db = mysqli_select_db($conn, $database);
        if (!$conn_db) {
            if (mysqli_errno($conn) == 1044) { 
                throw new DatabaseException('Access denied to the database. Please check the database name and permissions.');
            } else {
                throw new DatabaseException('Could not select the database. ' . mysqli_error($conn));
            }
        } else {
            echo "Connected.";
        }
    }
} catch (ServerException $ex) {
    echo "Error: " . $ex->getMessage();
} catch (DatabaseException $ex) {
    echo "Error: " . $ex->getMessage();
}
