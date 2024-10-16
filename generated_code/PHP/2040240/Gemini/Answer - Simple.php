<?php

function gen_uuid_v4() {
    // Generate random bytes using a cryptographically secure random number generator
    $bytes = random_bytes(16);

    // Set the version bits (4) in the correct position
    $bytes[6] = chr(ord($bytes[6]) & 0x0f | 0x40);

    // Set the variant bits (10xx) in the correct position
    $bytes[8] = chr(ord($bytes[8]) & 0x3f | 0x80);

    // Format the bytes into a UUID string
    return vsprintf('%s%s-%s-%s-%s-%s%s%s', str_split(bin2hex($bytes), 4));
}