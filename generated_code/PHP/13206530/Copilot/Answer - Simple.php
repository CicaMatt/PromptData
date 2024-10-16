<?php
function search_donar($post_data) {
    $by_name = $post_data['by_name'];
    $by_sex = $post_data['by_sex'];
    $by_group = $post_data['by_group'];
    $by_level = $post_data['by_level'];

    $conditions = [];

    if ($by_name != "") {
        $conditions[] = "name='$by_name'";
    }
    if ($by_sex != "") {
        $conditions[] = "sex='$by_sex'";
    }
    if ($by_group != "") {
        $conditions[] = "blood_group='$by_group'";
    }
    if ($by_level != "") {
        $conditions[] = "e_level='$by_level'";
    }

    $search_query = "SELECT * FROM donar";
    if (count($conditions) > 0) {
        $search_query .= " WHERE " . implode(" AND ", $conditions);
    }

    $result = mysql_query($search_query);

    return $result;
}
