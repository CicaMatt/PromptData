<?php
// Database connection (assumes PDO)
$pdo = new PDO('mysql:host=localhost;dbname=your_db', 'username', 'password');

// Define constants
define('MAX_FAILED_ATTEMPTS', 5);
define('LOCKOUT_TIME', 300); 
define('BASE_DELAY', 10); 
define('IP_BLOCK_THRESHOLD', 10); 

// Function to check login attempts
function checkLoginAttempts($username, $pdo) {
    $stmt = $pdo->prepare("SELECT failed_attempts, last_attempt, lockout_time FROM users WHERE username = :username");
    $stmt->execute(['username' => $username]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}

// Function to record failed login attempt
function recordFailedLogin($username, $pdo) {
    $stmt = $pdo->prepare("UPDATE users SET failed_attempts = failed_attempts + 1, last_attempt = NOW() WHERE username = :username");
    $stmt->execute(['username' => $username]);
}

// Function to reset login attempts after successful login
function resetLoginAttempts($username, $pdo) {
    $stmt = $pdo->prepare("UPDATE users SET failed_attempts = 0, lockout_time = NULL WHERE username = :username");
    $stmt->execute(['username' => $username]);
}

// Function to lock the user out if attempts exceed threshold
function lockOutUser($username, $pdo) {
    $stmt = $pdo->prepare("UPDATE users SET lockout_time = NOW() + INTERVAL :lockout_time SECOND WHERE username = :username");
    $stmt->execute(['lockout_time' => LOCKOUT_TIME, 'username' => $username]);
}

// Function to calculate delay based on failed attempts
function calculateDelay($attempts) {
    return BASE_DELAY * pow(2, $attempts - MAX_FAILED_ATTEMPTS); // exponential backoff
}

// Main login function
function login($username, $password, $pdo) {
    // Check user existence and failed attempts
    $loginData = checkLoginAttempts($username, $pdo);

    // Check if the user is locked out
    if ($loginData && $loginData['lockout_time'] > time()) {
        echo "Account locked. Please try again later.";
    }

    // Assume password check function, e.g. using password_verify() for hash comparison
    $passwordCorrect = checkPassword($username, $password, $pdo);

    if ($passwordCorrect) {
        resetLoginAttempts($username, $pdo);
        echo "Login successful!";
    } else {
        // If password is incorrect, record failed attempt
        recordFailedLogin($username, $pdo);

        $failedAttempts = $loginData['failed_attempts'] + 1;
        
        if ($failedAttempts >= MAX_FAILED_ATTEMPTS) {
            $delay = calculateDelay($failedAttempts);

            // Lock the user if they exceed threshold
            if ($failedAttempts >= IP_BLOCK_THRESHOLD) {
                lockOutUser($username, $pdo);
                echo "Too many failed attempts. Account temporarily locked.";
            }

            // Introduce a delay before responding
            sleep($delay);
            echo "Too many failed attempts. Please wait {$delay} seconds and try again.";
        } else {
            echo "Incorrect username or password. You have " . (MAX_FAILED_ATTEMPTS - $failedAttempts) . " more attempts.";
        }
    }
}

// Mock function to simulate password verification
function checkPassword($username, $password, $pdo) {
    $stmt = $pdo->prepare("SELECT password FROM users WHERE username = :username");
    $stmt->execute(['username' => $username]);
    $storedHash = $stmt->fetchColumn();
    
    return password_verify($password, $storedHash);
}

// Usage example (this would be triggered by your login form)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    login($username, $password, $pdo);
}
?>
