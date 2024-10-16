<?php
function search_donar($postData) {
    $by_name = $postData['by_name'] ?? null;
    $by_sex = $postData['by_sex'] ?? null;
    $by_group = $postData['by_group'] ?? null;
    $by_level = $postData['by_level'] ?? null;

    // Error handling for PDO connection
    try {
        $db = new PDO('mysql:host=localhost;dbname=your_database', 'username', $_ENV["SECRET"]);
        $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        
        $stmt = $db->prepare("SELECT * FROM donar WHERE name = :name AND sex = :sex AND blood_group = :group AND e_level = :level");
        $stmt->bindParam(':name', $by_name, PDO::PARAM_STR);
        $stmt->bindParam(':sex', $by_sex, PDO::PARAM_STR);
        $stmt->bindParam(':group', $by_group, PDO::PARAM_STR); // Assuming blood group is a string
        $stmt->bindParam(':level', $by_level, PDO::PARAM_STR); // Assuming e_level is a string
        $stmt->execute();

        return $stmt->fetchAll(PDO::FETCH_ASSOC); // Fetching results
    } catch (PDOException $e) {
        // Handle connection error
        echo "Connection failed: " . $e->getMessage();
        return false; // Return false on failure
    }
}
