<?php

$invoices = array(
    'invoice1.html',
    'invoice2.html',
    // Add more invoices here...
);

$pdfs = array();
foreach ($invoices as $invoice) {
    $tmpPdf = new WKPdf($this->_options);
    $tmpPdf->addPage($invoice, Array(
        'footer-html' => $this->footer
    ));
    $pdfs[] = $tmpPdf->toString();
}

$mergedPdf = new WKPdf($this->_options);
foreach ($pdfs as $pdf) {
    $mergedPdf->addPage($pdf, Array(
        'footer-html' => $this->footer
    ));
}

$filename = tempnam('', 'invoices.pdf');
file_put_contents($filename, $mergedPdf->toString());

echo "PDF generated successfully!";