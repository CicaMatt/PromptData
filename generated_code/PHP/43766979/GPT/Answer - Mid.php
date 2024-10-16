<?php
   // Database connection parameters
   $hostname = "localhost";
   $username = "root1";  // This is intentionally incorrect for demonstration
   $password = "";
   $database = "php_thenewboston"; // Make sure this is correct

   // Exception classes to handle specific errors
   class ServerException extends Exception{}  
   class DatabaseException extends Exception{}

   // Establish connection to MySQL server
   try {
       // Try to connect to the database server
       $conn = mysqli_connect($hostname, $username, $password);

       // Check if connection failed (incorrect username/password)
       if (!$conn) {
           throw new ServerException('Could not connect to server: ' . mysqli_connect_error());
       }

       // Try selecting the database
       $conn_db = mysqli_select_db($conn, $database);

       // Check if database selection failed (incorrect database name)
       if (!$conn_db) {
           throw new DatabaseException('Could not select database: ' . mysqli_error($conn));
       }

       // If no errors, connection and selection are successful
       echo "Connected successfully to the database.";

   } catch (ServerException $ex) {
       // Handle server (username/password) connection error
       echo "Error: " . $ex->getMessage();        
   } catch (DatabaseException $ex) {
       // Handle database selection error
       echo "Error: " . $ex->getMessage();
   }
?>
