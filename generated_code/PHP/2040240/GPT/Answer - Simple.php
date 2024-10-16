<?php

function gen_uuid() {
    $uuid = array(
        'time_low'  => 0,
        'time_mid'  => 0,
        'time_hi'  => 0,
        'clock_seq_hi' => 0,
        'clock_seq_low' => 0,
        'node'   => array()
    );

    // Generate time_low
    $uuid['time_low'] = mt_rand(0, 0xffff) + (mt_rand(0, 0xffff) << 16);

    // Generate time_mid
    $uuid['time_mid'] = mt_rand(0, 0xffff);

    // Generate time_hi with the version (4)
    $uuid['time_hi'] = (4 << 12) | (mt_rand(0, 0x0fff));

    // Generate clock_seq_hi with the variant (8, 9, A, or B)
    // Ensure the first two bits are 10 (binary), corresponding to 8, 9, A, or B
    $uuid['clock_seq_hi'] = (mt_rand(0, 0x3f) | 0x80); // 0x80 sets the first two bits to 10

    // Generate clock_seq_low
    $uuid['clock_seq_low'] = mt_rand(0, 255);

    // Generate node (MAC-like address, 48-bit)
    for ($i = 0; $i < 6; $i++) {
        $uuid['node'][$i] = mt_rand(0, 255);
    }

    // Format the UUID
    $uuid = sprintf('%08x-%04x-%04x-%02x%02x-%02x%02x%02x%02x%02x%02x',
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

?>
