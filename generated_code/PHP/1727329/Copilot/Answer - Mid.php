<?php
session_start();
require_once('config.php');  // Database credentials are now stored in an external file

// Create connection using secure database credentials
$conn = new mysqli(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_NAME);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Function to record a failed login attempt
function record_failed_attempt($conn, $user) {
    $stmt = $conn->prepare("INSERT INTO login_attempts (username, attempt_time) VALUES (?, NOW())");
    $stmt->bind_param("s", $user);
    $stmt->execute();
    $stmt->close();
}

// Function to get the number of failed login attempts in the last 5 minutes
function get_failed_attempts($conn, $user) {
    $stmt = $conn->prepare("SELECT COUNT(*) FROM login_attempts WHERE username = ? AND attempt_time > (NOW() - INTERVAL 5 MINUTE)");
    $stmt->bind_param("s", $user);
    $stmt->execute();
    $stmt->bind_result($count);
    $stmt->fetch();
    $stmt->close();
    return $count;
}

// Function to clear old login attempts (older than 5 minutes)
function clear_old_attempts($conn) {
    $stmt = $conn->prepare("DELETE FROM login_attempts WHERE attempt_time < (NOW() - INTERVAL 5 MINUTE)");
    $stmt->execute();
    $stmt->close();
}

// Main login logic
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $user = $_POST['username'];
    $pass = $_POST['password'];

    // Clear old failed attempts
    clear_old_attempts($conn);

    // Retrieve the hashed password for the given user
    $stmt = $conn->prepare("SELECT password FROM users WHERE username = ?");
    $stmt->bind_param("s", $user);
    $stmt->execute();
    $stmt->bind_result($hashed_password);
    $stmt->fetch();
    $stmt->close();

    if ($hashed_password && password_verify($pass, $hashed_password)) {
        // Successful login
        $_SESSION['username'] = $user;
        echo "Login successful!";
    } else {
        // Failed login: record the attempt
        record_failed_attempt($conn, $user);
        $failed_attempts = get_failed_attempts($conn, $user);

        // Implement exponential backoff for brute-force protection
        if ($failed_attempts >= 5) {
            $sleep_time = pow(2, $failed_attempts - 5); // Exponential backoff
            sleep($sleep_time);
        }

        echo "Login failed! You have made $failed_attempts failed attempts.";
    }
}

// Close the connection
$conn->close();
?>
