<?php

// Enable error reporting for development, should be disabled in production
error_reporting(E_ALL);
ini_set('display_errors', 1);

class Database
{
    private $conn;

    public function __construct($servername, $username, $password, $dbname)
    {
        $this->connect($servername, $username, $password, $dbname);
    }

    private function connect($servername, $username, $password, $dbname) 
    {
        $this->conn = new mysqli($servername, $username, $password, $dbname);

        if ($this->conn->connect_error) {
            $this->handleError("Connection failed: " . $this->conn->connect_error);
        }
    }

    public function getConnection()
    {
        return $this->conn;
    }

    public function closeConnection()
    {
        if ($this->conn) {
            $this->conn->close();
        }
    }

    private function handleError($message) 
    {
        error_log($message);
        echo "An error occurred. Please try again later.";
    }
}

class UserQuery 
{
    private $db;
    private $stmt;

    public function __construct($dbConnection) 
    {
        $this->db = $dbConnection;
    }

    public function fetchUserData($userid, $category) 
    {
        $query = "SELECT * FROM mytable WHERE userid = ? AND category = ? ORDER BY id DESC";

        $this->stmt = $this->db->prepare($query);
        if ($this->stmt === false) {
            $this->handleError("Error preparing the statement: " . $this->db->error);
        }

        // Bind parameters (userid is integer, category is string)
        $this->stmt->bind_param("is", $userid, $category);

        // Execute the statement
        if (!$this->stmt->execute()) {
            $this->handleError("Error executing the query: " . $this->stmt->error);
        }

        // Return the result set
        return $this->stmt->get_result();
    }

    public function closeStatement() 
    {
        if ($this->stmt) {
            $this->stmt->close();
        }
    }

    private function handleError($message) 
    {
        error_log($message);
        echo "An error occurred while processing your request.";
    }
}

class InputValidator 
{
    public static function sanitizeUserId() 
    {
        return filter_input(INPUT_GET, 'userid', FILTER_VALIDATE_INT) ?? 0;
    }

    public static function sanitizeCategory() 
    {
        return filter_input(INPUT_GET, 'category', FILTER_SANITIZE_STRING) ?? '';
    }
}

// Configuration (ideally, you'd load this from a config file)
$servername = "localhost";
$username = "username";
$password = "password";
$dbname = "mydatabase";

// Main program
try {
    // Initialize the database connection
    $db = new Database($servername, $username, $password, $dbname);

    // Sanitize inputs
    $userid = InputValidator::sanitizeUserId();
    $category = InputValidator::sanitizeCategory();

    // Initialize and run the query
    $query = new UserQuery($db->getConnection());
    $result = $query->fetchUserData($userid, $category);

    // Process the results
    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            echo "ID: " . htmlspecialchars($row['id']) . " - UserID: " . htmlspecialchars($row['userid']) . " - Category: " . htmlspecialchars($row['category']) . "<br>";
        }
    } else {
        echo "No results found.";
    }

} finally {
    // Ensure resources are always closed
    if (isset($query)) {
        $query->closeStatement();
    }
    if (isset($db)) {
        $db->closeConnection();
    }
}
?>
