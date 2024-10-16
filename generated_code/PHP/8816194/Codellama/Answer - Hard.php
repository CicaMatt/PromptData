<?php
$html = file_get_contents('https://example.com/table');
$dom = new DOMDocument();
$dom->loadHTML($html);

// Find all <td> elements in the table
$cols = $dom->getElementsByTagName('td');

// Extract the second column
$second_column = [];
foreach ($cols as $col) {
    if ($col->getAttribute('class') === 'second-column') {
        array_push($second_column, trim($col->nodeValue));
    }
}
print($second_column);