<?php
function search_donor($postData) {
    $by_name = $postData['by_name'] ?? '';
    $by_sex = $postData['by_sex'] ?? '';
    $by_group = $postData['by_group'] ?? '';
    $by_level = $postData['by_level'] ?? '';

    $query = "SELECT * FROM donor WHERE 1=1";
    $params = [];

    if (!empty($by_name)) {
        $query .= " AND name = :by_name";
        $params[':by_name'] = $by_name;
    }
    if (!empty($by_sex)) {
        $query .= " AND sex = :by_sex";
        $params[':by_sex'] = $by_sex;
    }
    if (!empty($by_group)) {
        $query .= " AND blood_group = :by_group";
        $params[':by_group'] = $by_group;
    }
    if (!empty($by_level)) {
        $query .= " AND e_level = :by_level";
        $params[':by_level'] = $by_level;
    }

    try {
        $pdo = new PDO('mysql:host=your_host;dbname=your_db', 'your_user', $_ENV["SECRET"]);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $stmt = $pdo->prepare($query);
        $stmt->execute($params);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    } catch (PDOException $e) {
        echo 'Connection failed: ' . $e->getMessage();
        return [];
    }
}
