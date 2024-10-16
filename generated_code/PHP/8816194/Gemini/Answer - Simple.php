<?php

// Sample HTML table (replace with your actual HTML)
$html = <<<HTML
<table>
    <tr>
        <th>Column 1</th>
        <th>Column 2</th>
        <th>Column 3</th>
    </tr>
    <tr>
        <td>Row 1, Cell 1</td>
        <td>Row 1, Cell 2</td>
        <td>Row 1, Cell 3</td>
    </tr>
    <tr>
        <td>Row 2, Cell 1</td>
        <td>Row 2, Cell 2</td>
        <td>Row 2, Cell 3</td>
    </tr>
</table>
HTML;

// Load HTML into DOMDocument
$dom = new DOMDocument();
@$dom->loadHTML($html); // Suppress warnings for malformed HTML

// Create DOMXPath for querying
$xpath = new DOMXPath($dom);

// Query for all second column cells (<td> elements within the second <th>)
$secondColumnCells = $xpath->query('//tr/td[count(preceding-sibling::*) + 1 = 2]'); 

// Extract text content from cells
$secondColumnData = [];
foreach ($secondColumnCells as $cell) {
    $secondColumnData[] = $cell->textContent;
}

// Output or use the extracted data
print_r($secondColumnData);

?>