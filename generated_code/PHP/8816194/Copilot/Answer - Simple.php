<?php
// Step 1: Connect to the database
$dsn = 'mysql:host=your_host;dbname=your_dbname';
$username = 'your_username';
$password = 'your_password';

try {
    $pdo = new PDO($dsn, $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Step 2: Prepare and execute the query
    $stmt = $pdo->prepare("SELECT column1, column2 FROM your_table");
    $stmt->execute();

    // Step 3: Fetch the second column
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        echo $row['column2'] . "<br>";
    }
} catch (PDOException $e) {
    echo 'Connection failed: ' . $e->getMessage();
}
?>
