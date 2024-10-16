<?php

// Function to fetch the second column from a given HTML table
function fetchSecondColumn($html)
{
    // Create a new DOMDocument object
    $dom = new DOMDocument();

    // Suppress warnings from malformed HTML and load the HTML content
    // Optionally you can clean/sanitize the input HTML to avoid injection attacks
    libxml_use_internal_errors(true); // Disable error reporting
    $dom->loadHTML($html);
    libxml_clear_errors(); // Clear any HTML parsing errors

    // Create an instance of DOMXPath to query the document
    $xpath = new DOMXPath($dom);

    // XPath to select all rows from the table (adjust if your table has specific IDs or classes)
    $rows = $xpath->query('//table//tr');

    // Array to store the second column data
    $secondColumnData = [];

    // Loop through each row
    foreach ($rows as $row) {
        // Fetch all columns (td) in the current row
        $columns = $row->getElementsByTagName('td');

        // Ensure the row has at least two columns
        if ($columns->length >= 2) {
            // Append the content of the second column to the array
            $secondColumnData[] = trim($columns->item(1)->textContent);
        }
    }

    // Return the array of second column values
    return $secondColumnData;
}

// Example HTML input (in a real-world scenario, you'd retrieve this dynamically)
$html = '
<table>
    <tr><td>Row1 Col1</td><td>Row1 Col2</td><td>Row1 Col3</td></tr>
    <tr><td>Row2 Col1</td><td>Row2 Col2</td><td>Row2 Col3</td></tr>
    <tr><td>Row3 Col1</td><td>Row3 Col2</td><td>Row3 Col3</td></tr>
</table>';

// Call the function and fetch the second column
$secondColumn = fetchSecondColumn($html);

// Output the second column values (for testing)
echo "Second column data:\n";
print_r($secondColumn);

?>
