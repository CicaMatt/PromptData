<?php
$table = <<<TABLE
  <table>
    <tr>
      <th>Column 1</th>
      <th>Column 2</th>
      <th>Column 3</th>
    </tr>
    <tr>
      <td>Value 1</td>
      <td>Value 2</td>
      <td>Value 3</td>
    </tr>
    <tr>
      <td>Value 4</td>
      <td>Value 5</td>
      <td>Value 6</td>
    </tr>
  </table>
TABLE;

$doc = new DOMDocument();
$doc->loadHTML($table);

// Find the second column of the table
$columns = $doc->getElementsByTagName('th');
$secondColumn = $columns->item(1);

// Print the text content of the second column
echo $secondColumn->textContent;
?>