<?php
$hostname = "localhost";
$username = "root1";
$password = "";
$database = "php_thenewboston";

$conn = mysqli_connect($hostname, $username, $password);
if (!$conn) {
    throw new ServerException('Could not connect to server.');
}

$query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name='$database'";
$result = mysqli_query($conn, $query);
if ($result === false) {
    throw new DatabaseException('Could not select database.');
} elseif (mysqli_num_rows($result) == 0) {
    throw new DatabaseException('Database does not exist.');
}