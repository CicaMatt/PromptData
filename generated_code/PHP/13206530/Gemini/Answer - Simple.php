<?php
function search_donar($post_data, $mysqli) {
    $by_name = $post_data['by_name'];
    $by_sex = $post_data['by_sex'];
    $by_group = $post_data['by_group'];
    $by_level = $post_data['by_level'];

    $search_query = "SELECT * FROM donar";
    $conditions = array(); // To store our conditions
    $params = array(); // To store our parameter values
    $types = ""; // To store types for bind_param

    if($by_name != "") {
        $conditions[] = "name = ?";
        $params[] = $by_name;
        $types .= "s"; // s for string
    }
    if($by_sex != "") {
        $conditions[] = "sex = ?";
        $params[] = $by_sex;
        $types .= "s"; // s for string
    }
    if($by_group != "") {
        $conditions[] = "blood_group = ?";
        $params[] = $by_group;
        $types .= "s"; // s for string
    }
    if($by_level != "") {
        $conditions[] = "e_level = ?";
        $params[] = $by_level;
        $types .= "s"; // s for string
    }

    // Join conditions with " AND " if there are multiple
    if (!empty($conditions)) {
        $search_query .= " WHERE " . implode(" AND ", $conditions);
    }

    $stmt = $mysqli->prepare($search_query);

    if (!empty($params)) {
        $stmt->bind_param($types, ...$params); // Unpacking parameters
    }

    $stmt->execute();
    $result = $stmt->get_result();

    return $result->fetch_all(MYSQLI_ASSOC); // Fetch all records as an associative array
}
