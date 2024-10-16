<?php
// Sample HTML content containing a table
$html = '
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
        <tr>
            <td>Row 3, Col 1</td>
            <td>Row 3, Col 2</td>
            <td>Row 3, Col 3</td>
        </tr>
    </table>
';

// Create a new DOMDocument instance
$dom = new DOMDocument();

// Load the HTML content (suppress warnings for invalid HTML)
@$dom->loadHTML($html);

// Create a new DOMXPath instance to navigate through the DOM
$xpath = new DOMXPath($dom);

// Define the XPath query to fetch the second column (index 2)
$query = "//tr/td[2]";

// Execute the query to get all second column values
$nodes = $xpath->query($query);

// Loop through the fetched nodes and print their values
foreach ($nodes as $node) {
    echo $node->nodeValue . "<br>";
}
?>
