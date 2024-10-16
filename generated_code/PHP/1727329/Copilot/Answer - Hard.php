<?php
session_start();
$max_attempts = 5;
$lockout_time = 300; // 5 minutes
$delay_increment = 2; // seconds

function check_login($username, $password) 
{
    // Replace with actual user authentication logic
    return $username === 'admin' && $password === 'password';
}

function record_failed_attempt($username) 
{
    $attempts = isset($_SESSION['failed_attempts']) ? $_SESSION['failed_attempts'] : [];
    $current_time = time();

    // Remove old attempts
    $attempts = array_filter($attempts, function($attempt) use ($current_time) {
        return ($current_time - $attempt) < $GLOBALS['lockout_time'];
    });

    // Add new attempt
    $attempts[] = $current_time;
    $_SESSION['failed_attempts'] = $attempts;

    return count($attempts);
}

function get_delay($attempt_count) 
{
    return ($attempt_count - $GLOBALS['max_attempts']) * $GLOBALS['delay_increment'];
}

$username = $_POST['username'];
$password = $_POST['password'];

if (check_login($username, $password)) {
    echo "Login successful!";
    unset($_SESSION['failed_attempts']);
} else {
    $attempt_count = record_failed_attempt($username);

    if ($attempt_count > $max_attempts) {
        $delay = get_delay($attempt_count);
        sleep($delay);
    }

    echo "Login failed!";
}
?>
