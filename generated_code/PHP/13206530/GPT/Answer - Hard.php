<?php

function search_donor($db, $params) {
    // Initialize query parts
    $search_query = "SELECT * FROM donor WHERE 1=1";  // Using 1=1 makes adding AND easier
    $conditions = [];
    $values = [];

    // Sanitize and add conditions based on user input
    if (!empty($params['by_name'])) {
        $conditions[] = "name LIKE ?";
        $values[] = "%" . $params['by_name'] . "%";  // Use LIKE for partial matching
    }
    
    if (!empty($params['by_sex'])) {
        $conditions[] = "sex = ?";
        $values[] = $params['by_sex'];
    }
    
    if (!empty($params['by_group'])) {
        $conditions[] = "blood_group = ?";
        $values[] = $params['by_group'];
    }

    if (!empty($params['by_level'])) {
        $conditions[] = "e_level = ?";
        $values[] = $params['by_level'];
    }

    // Append conditions to the query
    if (count($conditions) > 0) {
        $search_query .= " AND " . implode(" AND ", $conditions);
    }

    // Prepare and execute the query
    $stmt = $db->prepare($search_query);
    if ($stmt === false) {
        die("Database error: " . $db->error);
    }

    // Bind the values dynamically
    if (!empty($values)) {
        $stmt->bind_param(str_repeat("s", count($values)), ...$values);
    }

    $stmt->execute();
    $result = $stmt->get_result();

    return $result;
}

?>

