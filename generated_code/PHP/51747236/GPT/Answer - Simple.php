<?php
// Generate individual PDFs for each invoice
foreach ($invoices as $invoice) {
    $pdf = new WKPdf($options);
    $pdf->addPage($invoiceContent);
    $pdf->toFile($invoicePdfFilename); // Save each invoice as a separate PDF file
}

foreach ($invoicePdfFiles as $invoicePdfFilename) {
    // Get page count using pdfinfo
    $pageCount = getNumPages($invoicePdfFilename);

    // If the page count is odd, create a blank page and append it
    if ($pageCount % 2 !== 0) {
        // Generate a blank PDF (a single page)
        $blankPdfFilename = generateBlankPdf(); // Use wkhtmltopdf or another tool for this
        
        // Append blank page to the invoice PDF
        exec("pdftk $invoicePdfFilename $blankPdfFilename cat output $finalPdfFilename");
    } else {
        // No need to append, just use the original invoice PDF
        copy($invoicePdfFilename, $finalPdfFilename);
    }
}

// Merge all the individual PDFs (with blank pages where necessary) into a single PDF
$allPdfs = implode(' ', $finalPdfFilenames);
exec("pdftk $allPdfs cat output final_batch.pdf");
