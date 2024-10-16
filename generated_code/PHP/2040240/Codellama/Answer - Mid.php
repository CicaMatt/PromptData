<?php
function generate_uuid() {
    $uuid = [
        'time_low' => mt_rand(0, 0xffff) + (mt_rand(0, 0xffff) << 16),
        'time_mid' => mt_rand(0, 0xffff),
        'time_hi' => (4 << 12) | (mt_rand(0, 0x1000)),
        'clock_seq_hi' => (1 << 7) | (mt_rand(0, 128)),
        'clock_seq_low' => mt_rand(0, 255),
        'node' => [mt_rand(0, 255), mt_rand(0, 255), mt_rand(0, 255), mt_rand(0, 255), mt_rand(0, 255), mt_rand(0, 255)]
    ];

    return sprintf('%08x-%04x-%04x-%02x%02x-%02x%02x%02x%02x%02x%02x', $uuid['time_low'], $uuid['time_mid'], $uuid['time_hi'], $uuid['clock_seq_hi'], $uuid['clock_seq_low'], $uuid['node'][0], $uuid['node'][1], $uuid['node'][2], $uuid['node'][3], $uuid['node'][4], $uuid['node'][5]);
}