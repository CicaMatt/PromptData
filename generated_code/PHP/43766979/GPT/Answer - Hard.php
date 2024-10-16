<?php
// Database connection configuration (example)
$hostname = "localhost";
$username = "root1";  
$password = "";       
$database = "php_thenewboston";  

// Custom Exception Classes
class ServerException extends Exception {}
class DatabaseException extends Exception {}

try {
    // Establish a connection to the server (hostname, username, password)
    $conn = mysqli_connect($hostname, $username, $password);

    // Check if the connection to the server failed
    if (!$conn) {
        // Capture the specific connection error message
        throw new ServerException('Server connection error: ' . mysqli_connect_error());
    }

    // Attempt to select the specified database
    $conn_db = mysqli_select_db($conn, $database);

    // Check if the database selection failed
    if (!$conn_db) {
        // Capture the specific database selection error
        throw new DatabaseException('Database selection error: ' . mysqli_error($conn));
    }

    // If no exceptions were thrown, connection was successful
    echo "Connected successfully to the database.";
    
} catch (ServerException $ex) {
    // Handle server connection errors (hostname, username, password issues)
    // In a production environment, consider logging this error instead of showing it to the user
    echo "Error: " . $ex->getMessage();
    
} catch (DatabaseException $ex) {
    // Handle database selection errors
    // In a production environment, you may want to log the error instead of displaying it directly
    echo "Error: " . $ex->getMessage();
}

// Optionally close the database connection
if ($conn) {
    mysqli_close($conn);
}
?>
