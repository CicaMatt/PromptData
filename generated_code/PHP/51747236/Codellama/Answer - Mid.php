<?php
$options = array(
    'binary' => '/path/to/wkhtmltopdf',
    'process-single-page' => true,
);
$pdf = new WKPdf($options);

// Add all the templates to the PDF
foreach ($templates as $template) {
    $pdf->addPage($template['content'], $template['footer']);
}

// Generate the PDF file
$pdf->toString();