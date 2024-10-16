<?php
$hostname = "localhost";
$username = "root1";
$password = "";
$database = "php_thenewboston";

class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    $conn = mysqli_connect($hostname, $username, $password);

    if (mysqli_connect_errno()) { 
        if (mysqli_connect_errno() == 1045) { 
            throw new ServerException('Access denied. Please check your username and password.');
        } else {
            throw new ServerException('Could not connect to the server: ' . mysqli_connect_error());
        }
    }

    $conn_db = mysqli_select_db($conn, $database);

    if (!$conn_db) {
        throw new DatabaseException('Could not select the database. Please check the database name.');
    } else {
        echo "Connected.";
    }
} catch (ServerException $ex) {
    echo "Error: " . $ex->getMessage();
} catch (DatabaseException $ex) {
    echo "Error: " . $ex->getMessage();
}
