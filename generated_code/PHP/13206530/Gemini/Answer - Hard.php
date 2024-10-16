<?php

function search_donar($by_name = '', $by_sex = '', $by_group = '', $by_level = '') {
    // Database connection (replace with your actual credentials)
    $dsn = 'mysql:host=your_host;dbname=your_database;charset=utf8mb4';
    $username = 'your_username';
    $password = 'your_password'; 
    $options = [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES => false,
    ];

    try {
        $pdo = new PDO($dsn, $username, $password, $options); 
    } catch (PDOException $e) {
        die("Database connection failed: " . $e->getMessage());
    }

    $search_query = "SELECT * FROM donar WHERE 1=1"; 
    $params = [];

    if (!empty($by_name)) {
        $search_query .= " AND name LIKE :name";
        $params[':name'] = '%' . $by_name . '%'; 
    }
    if (!empty($by_sex)) {
        $search_query .= " AND sex = :sex";
        $params[':sex'] = $by_sex;
    }
    if (!empty($by_group)) {
        $search_query .= " AND blood_group = :blood_group";
        $params[':blood_group'] = $by_group;
    }
    if (!empty($by_level)) {
        $search_query .= " AND e_level = :e_level";
        $params[':e_level'] = $by_level;
    }

    try {
        $stmt = $pdo->prepare($search_query);
        $stmt->execute($params);
        $result = $stmt->fetchAll();
        return $result;
    } catch (PDOException $e) {
        die("Query execution failed: " . $e->getMessage());
    }
}

if (isset($_POST['submit'])) {
    // Extracting the parameters from $_POST to pass them to the function
    $by_name = isset($_POST['by_name']) ? $_POST['by_name'] : '';
    $by_sex = isset($_POST['by_sex']) ? $_POST['by_sex'] : '';
    $by_group = isset($_POST['by_group']) ? $_POST['by_group'] : '';
    $by_level = isset($_POST['by_level']) ? $_POST['by_level'] : '';

    $retrieved_result = search_donar($by_name, $by_sex, $by_group, $by_level);
    
    // Process and display the results here
    if ($retrieved_result) {
        // Display results (example)
        foreach ($retrieved_result as $row) {
            echo 'Name: ' . htmlspecialchars($row['name']) . '<br>';
            echo 'Sex: ' . htmlspecialchars($row['sex']) . '<br>';
            echo 'Blood Group: ' . htmlspecialchars($row['blood_group']) . '<br>';
            echo 'Level: ' . htmlspecialchars($row['e_level']) . '<br><br>';
        }
    } else {
        echo 'No results found.';
    }
}
?>
