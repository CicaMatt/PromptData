<?php

// ... (Database connection, etc.)

function checkLogin($username, $password) {
    // ... (Your existing login check logic)

    if ($loginSuccessful) {
        // ... (Allow login)
    } else {
        // Record failed attempt
        recordFailedAttempt($username); 

        // Check for too many attempts
        $failedAttempts = getFailedAttempts($username);
        if ($failedAttempts >= 5) {
            $sleepTime = calculateSleepTime($failedAttempts); // Exponential backoff
            sleep($sleepTime); 
        }

        // ... (Report login failure to user)
    }
}

function calculateSleepTime($failedAttempts) {
    // Example of exponential backoff:
    return pow(2, $failedAttempts - 5) * 10; // Starts at 10 seconds, doubles with each additional attempt
}

// ... (Rest of your code)