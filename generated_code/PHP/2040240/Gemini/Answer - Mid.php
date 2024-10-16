<?php

function gen_uuid() {
    // Version 4 UUIDs have a fixed '4' in the 3rd group
    $time_hi_and_version = (4 << 12) | mt_rand(0, 0x0fff);

    // Variant 1 UUIDs have the first two bits of the clock_seq_hi set to '10'
    $clock_seq_hi_and_reserved = (1 << 7) | (mt_rand(0, 0x3f));

    // Use random_bytes for cryptographically secure random data
    $time_low = bin2hex(random_bytes(4));
    $time_mid = bin2hex(random_bytes(2));
    $clock_seq_low = bin2hex(random_bytes(1));
    $node = bin2hex(random_bytes(6));

    return sprintf('%08s-%04s-%04x-%02x%02s-%012s',
        $time_low,
        $time_mid,
        $time_hi_and_version,
        $clock_seq_hi_and_reserved,
        $clock_seq_low,
        $node
    );
}