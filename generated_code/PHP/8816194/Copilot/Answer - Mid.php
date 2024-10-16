<?php
// Load the HTML content
$html = '<table>
            <tr>
                <td>Row 1, Column 1</td>
                <td>Row 1, Column 2</td>
            </tr>
            <tr>
                <td>Row 2, Column 1</td>
                <td>Row 2, Column 2</td>
            </tr>
        </table>';

$dom = new DOMDocument;
libxml_use_internal_errors(true); // Disable libxml errors
$dom->loadHTML($html);
libxml_clear_errors(); // Clear libxml errors

// Get all rows
$rows = $dom->getElementsByTagName('tr');

$secondColumnData = [];

// Loop through each row and get the second column
foreach ($rows as $row) {
    $columns = $row->getElementsByTagName('td');
    if ($columns->length > 1) {
        $secondColumnData[] = $columns->item(1)->textContent;
    }
}

// Print the second column data
foreach ($secondColumnData as $data) {
    echo $data . "\n";
}
?>
