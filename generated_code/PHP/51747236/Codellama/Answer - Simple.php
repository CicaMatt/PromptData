<?php
$num_pages = array(); // array to store number of pages for each template
$pdf = new WKPdf($this->_options); // create instance of wrapper class
for ($i = 0; $i < count($templates); $i++) {
    $template = $templates[$i];
    $pdf->addPage($template, array(
        'footer-html' => $this->footer
    ));
    // Generate PDF of current template
    $tmpPdf = new WKPdf($this->_options);
    $tmpPdf->addPage($template, array(
        'footer-html' => $this->footer
    ));
    $tmpPdf->toString();
    // Get number of pages for current template
    $document = $tmpPdf->getPdfFilename();
    $cmd = 'pdfinfo';
    exec("$cmd $document", $output);
    foreach ($output as $op) {
        if (preg_match("/Pages:\s*(\d+)/i", $op, $matches) === 1) {
            $num_pages[$i] = intval($matches[1]);
            break;
        }
    }
}
// Insert blank pages as necessary for double-sided printing
for ($i = 0; $i < count($templates); $i++) {
    if ($num_pages[$i] % 2 != 0) { // if number of pages is odd
        $pdf->addPage('', array(
            'footer-html' => $this->footer
        ));
    }
}
// Generate final PDF file
$pdf->toString();