<?php

function gen_uuid_v4() {
    $uuid = array(
        'time_low' => 0,
        'time_mid' => 0,
        'time_hi' => 0,
        'clock_seq_hi' => 0,
        'clock_seq_low' => 0,
        'node' => array()
    );

    // Generate random values for time_low and time_mid
    $uuid['time_low'] = mt_rand(0, 0xffff) + (mt_rand(0, 0xffff) << 16);
    $uuid['time_mid'] = mt_rand(0, 0xffff);

    // Set the version to 4 (for v4 UUID) in the time_hi field
    $uuid['time_hi'] = (4 << 12) | (mt_rand(0, 0x0fff));

    // Set the variant to one of 8, 9, A, or B
    // Variant 1 (used in v4 UUIDs) is represented by the two most significant bits being 10
    $uuid['clock_seq_hi'] = (mt_rand(0, 0x3f) | 0x80); // Set the two MSBs to '10'

    // Generate the remaining parts
    $uuid['clock_seq_low'] = mt_rand(0, 255);
    for ($i = 0; $i < 6; $i++) {
        $uuid['node'][$i] = mt_rand(0, 255);
    }

    // Format the UUID as a string
    $uuid = sprintf(
        '%08x-%04x-%04x-%02x%02x-%02x%02x%02x%02x%02x%02x',
        $uuid['time_low'],
        $uuid['time_mid'],
        $uuid['time_hi'],
        $uuid['clock_seq_hi'],
        $uuid['clock_seq_low'],
        $uuid['node'][0],
        $uuid['node'][1],
        $uuid['node'][2],
        $uuid['node'][3],
        $uuid['node'][4],
        $uuid['node'][5]
    );

    return $uuid;
}

// Example usage:
echo gen_uuid_v4();
?>
