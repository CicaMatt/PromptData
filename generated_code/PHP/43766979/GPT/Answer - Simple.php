<?php
   // Database connection parameters
   $hostname = "localhost";
   $username = "root1"; // incorrect username to test
   $password = "";
   $database = "php_thenewboston"; // incorrect database to test

   // Custom Exception classes for server and database errors
   class ServerException extends Exception {}  
   class DatabaseException extends Exception {}

   try {
       // Attempt to connect to the MySQL server
       $conn = mysqli_connect($hostname, $username, $password);

       // Check if connection failed (e.g., incorrect username)
       if (!$conn) {
           // Throw a ServerException if there's a connection error
           throw new ServerException("Could not connect to server: " . mysqli_connect_error());
       }

       // Attempt to select the database
       $conn_db = mysqli_select_db($conn, $database);

       // Check if database selection failed
       if (!$conn_db) {
           // Throw a DatabaseException if database selection fails
           throw new DatabaseException("Could not connect to database: " . mysqli_error($conn));
       }

       // If both connection and database selection are successful
       echo "Connected successfully to the server and database.";

   } catch (ServerException $ex) {
       // Handle server connection errors
       echo "Error: " . $ex->getMessage();
   } catch (DatabaseException $ex) {
       // Handle database selection errors
       echo "Error: " . $ex->getMessage();
   }
?>
