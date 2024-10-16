<?php
// Enable error reporting for development (remove in production)
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Database credentials (ensure these are stored securely in production, not hardcoded)
$host = 'localhost';
$username = 'your_username';
$password = 'your_password';
$database = 'your_database_name';

// Function to handle errors
function handleError($message) {
    error_log($message);
    echo "An error occurred. Please try again later.";
}

// Secure Database Connection
$mysqli = new mysqli($host, $username, $password, $database);

// Check connection
if ($mysqli->connect_error) {
    handleError("Connection failed: " . $mysqli->connect_error);
}

// Function to validate and sanitize input
function getSanitizedInput($input, $pattern = '/^[a-zA-Z0-9]+$/')
{
    if (isset($input) && preg_match($pattern, $input)) {
        return trim($input);
    }
    return null;
}

// Get and sanitize input parameters
$userid = getSanitizedInput($_GET['userid'] ?? null);
$category = getSanitizedInput($_GET['category'] ?? null);

// Check if userid and category are present and valid
if (!$userid || !$category) {
    handleError("Invalid input.");
}

// Secure SQL query using prepared statements
$query = "SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC";

// Prepare the SQL statement
if ($stmt = $mysqli->prepare($query)) {

    // Bind the parameters to the statement
    $stmt->bind_param("ss", $userid, $category);

    // Execute the prepared statement
    if ($stmt->execute()) {
        // Fetch the results
        $result = $stmt->get_result();

        // Check if any rows were returned
        if ($result->num_rows > 0) {
            // Fetch and process each row
            while ($row = $result->fetch_assoc()) {
                // Output or process the result row (this could be customized)
                echo "Row: " . htmlspecialchars(print_r($row, true)) . "<br>";
            }
        } else {
            echo "No records found.";
        }

    } else {
        handleError("Query execution failed: " . $stmt->error);
    }

    // Close the statement
    $stmt->close();

} else {
    handleError("Failed to prepare the statement: " . $mysqli->error);
}

// Close the database connection
$mysqli->close();
?>
