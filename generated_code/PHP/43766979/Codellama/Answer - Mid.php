<?php
$hostname = "localhost";
$username = "root1";
$password = "";
$database = "php_thenewboston";

// Connect to the database
try {
  $conn = mysqli_connect($hostname, $username, $password);
} catch (Exception $e) {
  // If there was an error connecting to the server or selecting the database, throw a custom exception
  throw new ServerException('Could not connect to server.', 0, $e);
}

try {
  $conn_db = mysqli_select_db($conn, $database);
} catch (Exception $e) {
  // If there was an error selecting the database, throw a custom exception
  throw new DatabaseException('Could not connect to database.', 0, $e);
}

// If both connections were successful, echo a message indicating that the connection was made
echo "Connected.";

?>