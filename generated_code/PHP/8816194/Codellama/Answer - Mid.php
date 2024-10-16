<?php

// Get the HTML content of the page you want to parse
$html = file_get_contents('http://example.com');

// Create a new DOMDocument object and load the HTML content
$doc = new DOMDocument();
$doc->loadHTML($html);

// Find all tables on the page
$tables = $doc->getElementsByTagName('table');

// Loop through each table and find the second column
foreach ($tables as $table) {
  $rows = $table->getElementsByTagName('tr');
  foreach ($rows as $row) {
    $cells = $row->getElementsByTagName('td');
    if (count($cells) >= 2) {
      // The second cell is the second column
      echo $cells[1]->textContent . "\n";
    }
  }
}