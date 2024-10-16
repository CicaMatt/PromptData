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

    // Generate random values using cryptographically secure random_int()
    $uuid['time_low'] = random_int(0, 0xffff) + (random_int(0, 0xffff) << 16);
    $uuid['time_mid'] = random_int(0, 0xffff);
    $uuid['time_hi'] = (4 << 12) | (random_int(0, 0x0fff)); // Ensure the version is '4'
    
    // Generate the clock sequence (high variant)
    // Ensure the variant is '8', '9', 'A', or 'B' by masking the two high bits with 0b10xx
    $uuid['clock_seq_hi'] = (random_int(0, 0x3f) | 0x80); // 0x80 guarantees the first two bits are '10'
    $uuid['clock_seq_low'] = random_int(0, 0xff);

    // Generate node (MAC address or random)
    for ($i = 0; $i < 6; $i++) {
        $uuid['node'][$i] = random_int(0, 255);
    }

    // Format the UUID according to the specification
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

// Example usage
echo gen_uuid_v4();

?>
