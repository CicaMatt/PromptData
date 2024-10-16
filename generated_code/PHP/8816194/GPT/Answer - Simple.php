<?php
// Sample HTML (you can also load this from a file using file_get_contents)
$html = '
<table>
  <tr>
    <th>Header 1</th>
    <th>Header 2</th>
  </tr>
  <tr>
    <td>Row 1, Col 1</td>
    <td>Row 1, Col 2</td>
  </tr>
  <tr>
    <td>Row 2, Col 1</td>
    <td>Row 2, Col 2</td>
  </tr>
</table>';

// Load the HTML into a DOMDocument
$dom = new DOMDocument();
@$dom->loadHTML($html);  // @ is used to suppress warnings about malformed HTML

// Create a new DOMXPath object
$xpath = new DOMXPath($dom);

// Find all the <td> elements in the second column (index 2 in XPath as it's 1-based)
$nodes = $xpath->query('//table//tr/td[2]');

// Loop through the results and print them
foreach ($nodes as $node) {
    echo $node->nodeValue . "\n";
}
?>
