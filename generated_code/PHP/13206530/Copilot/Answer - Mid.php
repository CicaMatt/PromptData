<?php
function search_donor($postData, $conn) {
    $by_name = $postData['by_name'];
    $by_sex = $postData['by_sex'];
    $by_group = $postData['by_group'];
    $by_level = $postData['by_level'];

    $search_query = "SELECT * FROM donor WHERE 1=1";
    $params = [];
    $types = '';

    if ($by_name != "") {
        $search_query .= " AND name = ?";
        $params[] = $by_name;
        $types .= 's';
    }
    if ($by_sex != "") {
        $search_query .= " AND sex = ?";
        $params[] = $by_sex;
        $types .= 's';
    }
    if ($by_group != "") {
        $search_query .= " AND blood_group = ?";
        $params[] = $by_group;
        $types .= 's';
    }
    if ($by_level != "") {
        $search_query .= " AND e_level = ?";
        $params[] = $by_level;
        $types .= 's';
    }

    $stmt = $conn->prepare($search_query);
    if ($types) {
        $stmt->bind_param($types, ...$params);
    }
    $stmt->execute();
    $result = $stmt->get_result();

    return $result;
}
