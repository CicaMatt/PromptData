<?php

function fetchSecondColumn($html) {
  // 1. Secure Parsing: Use a robust HTML parser
  $dom = new DOMDocument();
  // Suppress warnings in case of minor HTML inconsistencies
  @$dom->loadHTML($html);

  $xpath = new DOMXPath($dom);

  // 2. Target the Second Column: Precise XPath query
  $secondColumnCells = $xpath->query('//table/tr/td[2]');

  $columnData = [];
  foreach ($secondColumnCells as $cell) {
    // 3. Sanitize Data: Prevent XSS vulnerabilities
    $columnData[] = htmlspecialchars($cell->textContent, ENT_QUOTES, 'UTF-8');
  }

  return $columnData;
}

// Example Usage:
$html = '<table>
  <tr><td>First</td><td>Second</td><td>Third</td></tr>
  <tr><td>1</td><td>2</td><td>3</td></tr>
  <tr><td>A</td><td>B</td><td>C</td></tr>
</table>';

$secondColumn = fetchSecondColumn($html);

print_r($secondColumn); // Output: Array ( [0] => Second [1] => 2 [2] => B )