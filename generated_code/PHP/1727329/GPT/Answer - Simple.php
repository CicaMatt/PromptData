<?php
session_start();
$maxAttempts = 5;
$lockoutTime = 5 * 60; // 5 minutes
$ipAddress = $_SERVER['REMOTE_ADDR']; // Get user's IP address

// Simulated function to get failed login attempts and timestamps from DB
function getFailedLoginAttempts($ipAddress) {
    // Query the database to get the number of failed attempts for this IP within the last 5 minutes
    // Here, we'll simulate it with a session value
    return isset($_SESSION['failed_attempts']) ? $_SESSION['failed_attempts'] : [];
}

// Simulated function to log a failed attempt to the DB
function logFailedAttempt($ipAddress) {
    // Add the failed attempt with the current timestamp
    if (!isset($_SESSION['failed_attempts'])) {
        $_SESSION['failed_attempts'] = [];
    }
    $_SESSION['failed_attempts'][] = time();
}

// Check login function (pseudo code)
function checkLogin($username, $password) {
    // Your logic for checking credentials
    return false; // For illustration, assume login fails
}

// Check failed attempts in the last 5 minutes
$failedAttempts = getFailedLoginAttempts($ipAddress);
$recentFailedAttempts = array_filter($failedAttempts, function($timestamp) use ($lockoutTime) {
    return time() - $timestamp < $lockoutTime;
});

if (count($recentFailedAttempts) >= $maxAttempts) {
    // Calculate increasing sleep time (exponential backoff)
    $delay = pow(2, count($recentFailedAttempts) - $maxAttempts); // 2, 4, 8... seconds
    sleep($delay); // Delay execution to slow down brute-force attempts
    
    echo "Too many failed attempts. Please wait $delay seconds and try again.";
    exit();
}

$username = $_POST['username'];
$password = $_POST['password'];

// Now check if the login is successful
if (checkLogin($username, $password)) {
    echo "Login successful!";
    // Clear the failed attempts on successful login
    unset($_SESSION['failed_attempts']);
} else {
    logFailedAttempt($ipAddress);
    echo "Invalid credentials. Please try again.";
}
?>
