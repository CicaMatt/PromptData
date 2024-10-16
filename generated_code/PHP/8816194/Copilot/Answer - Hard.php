<?php
// Load the HTML content
$html = <<<HTML
<table>
    <tr>
        <td>Row 1, Column 1</td>
        <td>Row 1, Column 2</td>
    </tr>
    <tr>
        <td>Row 2, Column 1</td>
        <td>Row 2, Column 2</td>
    </tr>
</table>
HTML;

// Create a new DOMDocument instance
$dom = new DOMDocument;

// Suppress errors due to malformed HTML
libxml_use_internal_errors(true);

// Load the HTML into the DOMDocument
$dom->loadHTML($html);

// Clear the errors
libxml_clear_errors();

// Get all the table rows
$rows = $dom->getElementsByTagName('tr');

// Initialize an array to hold the second column data
$secondColumnData = [];

// Loop through each row
foreach ($rows as $row) {
    // Get all the cells in the row
    $cells = $row->getElementsByTagName('td');
    
    // Check if the row has at least two cells
    if ($cells->length > 1) {
        // Add the second cell's text content to the array
        $secondColumnData[] = $cells->item(1)->textContent;
    }
}

// Output the second column data
foreach ($secondColumnData as $data) {
    echo $data . "\n";
}
?>
