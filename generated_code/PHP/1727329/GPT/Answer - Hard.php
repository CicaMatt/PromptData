<?php
session_start();
include 'db.php'; // Assume this file connects to your database

// Constants for limits
define('MAX_ATTEMPTS', 5);
define('LOCKOUT_TIME', 10);
define('PENALTY_MULTIPLIER', 2);
define('BLOCK_PERIOD', 300);

function get_failed_attempts($username) {
    global $db;
    try {
        $stmt = $db->prepare("SELECT attempts, last_attempt FROM users WHERE username = ?");
        $stmt->execute([$username]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    } catch (PDOException $e) {
        error_log($e->getMessage());
        return null;
    }
}

function record_failed_attempt($username) {
    global $db;
    try {
        $stmt = $db->prepare("UPDATE users SET attempts = attempts + 1, last_attempt = NOW() WHERE username = ?");
        $stmt->execute([$username]);
    } catch (PDOException $e) {
        error_log($e->getMessage());
    }
}

function reset_attempts($username) {
    global $db;
    try {
        $stmt = $db->prepare("UPDATE users SET attempts = 0, last_attempt = NULL WHERE username = ?");
        $stmt->execute([$username]);
    } catch (PDOException $e) {
        error_log($e->getMessage());
    }
}

function login($username, $password) {
    global $db;
    try {
        $stmt = $db->prepare("SELECT * FROM users WHERE username = ?");
        $stmt->execute([$username]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        
        return $user && password_verify($password, $user['password']);
    } catch (PDOException $e) {
        error_log($e->getMessage());
        return false;
    }
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = filter_input(INPUT_POST, 'username', FILTER_SANITIZE_STRING);
    $password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);

    if (!$username || !$password) {
        echo "Invalid input.";
        exit;
    }

    $attempt_data = get_failed_attempts($username);
    
    if ($attempt_data) {
        $attempts = (int) $attempt_data['attempts'];
        $last_attempt_time = strtotime($attempt_data['last_attempt']);
        $time_since_last_attempt = time() - $last_attempt_time;

        if ($attempts >= MAX_ATTEMPTS && $time_since_last_attempt < BLOCK_PERIOD) {
            $delay = LOCKOUT_TIME * pow(PENALTY_MULTIPLIER, $attempts - MAX_ATTEMPTS);
            sleep($delay);
            echo "Too many failed attempts. Please wait before trying again.";
        } else {
            if (login($username, $password)) {
                reset_attempts($username);
                echo "Login successful!";
            } else {
                record_failed_attempt($username);
                echo "Login failed. Please try again.";
            }
        }
    } else {
        if (login($username, $password)) {
            reset_attempts($username);
            echo "Login successful!";
        } else {
            record_failed_attempt($username);
            echo "Login failed. Please try again.";
        }
    }
}
?>
