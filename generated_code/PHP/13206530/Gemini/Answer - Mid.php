<?php
function search_donar($postData) {
    $byName = $postData['by_name'];
    $bySex = $postData['by_sex'];
    $byGroup = $postData['by_group'];
    $byLevel = $postData['by_level'];

    $searchQuery = "SELECT * FROM donar WHERE 1=1"; // Start with a true condition
    $conditions = [];

    if (!empty($byName)) {
        $conditions[] = "name LIKE ?"; 
    }
    if (!empty($bySex)) {
        $conditions[] = "sex = ?";
    }
    if (!empty($byGroup)) {
        $conditions[] = "blood_group = ?";
    }
    if (!empty($byLevel)) {
        $conditions[] = "e_level = ?";
    }

    if (!empty($conditions)) {
        $searchQuery .= " AND " . implode(" AND ", $conditions);
    }

    $stmt = $this->db->prepare($searchQuery); 

    $bindParams = [];
    if (!empty($byName)) {
        $bindParams[] = "%$byName%"; 
    }
    if (!empty($bySex)) {
        $bindParams[] = $bySex;
    }
    if (!empty($byGroup)) {
        $bindParams[] = $byGroup;
    }
    if (!empty($byLevel)) {
        $bindParams[] = $byLevel;
    }

    // Dynamically bind parameters based on types
    $types = str_repeat('s', count($bindParams)); // Assuming all are strings, adjust if needed
    $stmt->bind_param($types, ...$bindParams);

    $stmt->execute();
    $result = $stmt->get_result();

    return $result;
}