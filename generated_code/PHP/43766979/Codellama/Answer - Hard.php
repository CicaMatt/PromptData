<?php
$hostname = 'localhost';
$username = 'root1';
$password = '';
$database = 'php_thenewboston';

try {
  $conn = mysqli_connect($hostname, $username, $password);
  if (!$conn) {
    throw new ServerException('Could not connect to server.');
  }
  $conn_db = mysqli_select_db($conn, $database);
  if (!$conn_db) {
    throw new DatabaseException('Could not connect to database.');
  }
  echo "Connected.";
} catch (ServerException $ex) {
  echo "Error :" . $ex->getMessage();
} catch (DatabaseException $ex) {
  echo "Error :" . $ex->getMessage();
}
?>