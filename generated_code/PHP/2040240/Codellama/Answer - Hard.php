<?php
function gen_uuid() {
    $uuid = array(
        'time_low' => random_int(0, 0xffff) + (random_int(0, 0xffff) << 16),
        'time_mid' => random_int(0, 0xffff),
        'time_hi' => (4 << 12) | (random_int(0, 0x1000)),
        'clock_seq_hi' => (1 << 7) | (random_int(0, 128)),
        'clock_seq_low' => random_int(0, 255),
        'node' => array()
    );
    
    for ($i = 0; $i < 6; $i++) {
        $uuid['node'][$i] = random_int(1, 255);
    }
    
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