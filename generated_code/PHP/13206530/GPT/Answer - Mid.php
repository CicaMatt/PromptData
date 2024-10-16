<?php
function search_donar($connection, $postData) {
    // Initializing query components
    $search_query = "SELECT * FROM donar WHERE 1=1";
    $params = [];
    $types = '';

    // Check for filters and build the query dynamically
    if (!empty($postData['by_name'])) {
        $search_query .= " AND name = ?";
        $params[] = $postData['by_name'];
        $types .= 's'; // 's' stands for string type in prepared statements
    }
    if (!empty($postData['by_sex'])) {
        $search_query .= " AND sex = ?";
        $params[] = $postData['by_sex'];
        $types .= 's';
    }
    if (!empty($postData['by_group'])) {
        $search_query .= " AND blood_group = ?";
        $params[] = $postData['by_group'];
        $types .= 's';
    }
    if (!empty($postData['by_level'])) {
        $search_query .= " AND e_level = ?";
        $params[] = $postData['by_level'];
        $types .= 's';
    }

    // Prepare the query
    $stmt = $connection->prepare($search_query);

    if (!empty($params)) {
        // Dynamically bind the parameters to the statement
        $stmt->bind_param($types, ...$params);
    }

    // Execute the query
    $stmt->execute();

    // Fetch the results
    $result = $stmt->get_result();

    return $result;
}

// Example of form submission handling
if (isset($_POST['submit'])) {
    $retrived_result = search_donar($connection, $_POST); // Pass the database connection
}
?>
