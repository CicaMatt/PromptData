<?php

// Sample HTML Table (Replace with your actual table or fetch it from a file/URL)
$htmlTable = <<<HTML
<table>
    <tr>
        <th>Header 1</th>
        <th>Header 2</th>
        <th>Header 3</th>
    </tr>
    <tr>
        <td>Row 1, Col 1</td>
        <td>Row 1, Col 2</td>
        <td>Row 1, Col 3</td>
    </tr>
    <tr>
        <td>Row 2, Col 1</td>
        <td>Row 2, Col 2</td>
        <td>Row 2, Col 3</td>
    </tr>
</table>
HTML;

// 1. Load HTML into DOMDocument
$dom = new DOMDocument();
// Suppress warnings for potentially malformed HTML
@$dom->loadHTML($htmlTable);

// 2. Create DOMXPath for querying
$xpath = new DOMXPath($dom);

// 3. Query for all second column cells (td elements)
$secondColumnCells = $xpath->query('//tr/td[2]');

// 4. Extract and store the text content of each cell
$secondColumnData = [];
foreach ($secondColumnCells as $cell) {
    $secondColumnData[] = $cell->textContent;
}

// 5. Output the extracted data
print_r($secondColumnData);