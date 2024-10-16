<?php
$userid = $_GET['userid'];
$category = $_GET['category'];

// Create a new PDO object
$pdo = new PDO('mysql:host=localhost;dbname=mydatabase', 'username', 'password');

// Prepare the SQL query
$stmt = $pdo->prepare("SELECT * FROM mytable WHERE userid = :userid AND category = :category ORDER BY id DESC");

// Bind the input parameters to the prepared statement
$stmt->bindParam(':userid', $userid);
$stmt->bindParam(':category', $category);

// Execute the query
$result = $stmt->execute();