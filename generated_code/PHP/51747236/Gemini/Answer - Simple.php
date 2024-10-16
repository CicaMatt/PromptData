<?php
function generateBatchPdf() 
{
    $pageCounts = [];
    $totalBatchPages = 0;

    // Preliminary page count estimation (optional)
    foreach ($this->content as $invoiceContent) {
        // Estimate page count based on content length or other heuristics (if possible)
        $estimatedPageCount = estimatePageCount($invoiceContent);
        $pageCounts[] = $estimatedPageCount;
        $totalBatchPages += $estimatedPageCount;
    }

    // Generate the batch PDF
    $this->createPdf();

    // Retrieve actual page counts & adjust if needed
    $actualPageCount = $this->getNumPages(true); // Get total pages of generated batch
    $currentPage = 1;

    for ($i = 0; $i < count($this->content); $i++) {
        $actualInvoicePages = 0; 

        // Determine actual pages for this invoice
        while ($currentPage <= $actualPageCount && $actualInvoicePages < $pageCounts[$i]) {
            $actualInvoicePages++;
            $currentPage++;
        }

        // Add blank page if needed for duplex printing
        if ($actualInvoicePages % 2 == 1) {
            $this->addPage('', Array('footer-html' => '')); // Add a blank page
            $totalBatchPages++;
        }
    }

    // Regenerate final PDF with adjustments (if any)
    if ($totalBatchPages != $actualPageCount) {
        $this->createPdf(); 
    }
}

// Helper function to estimate page count (implementation depends on your content)
function estimatePageCount($content) {
    // ... your logic to estimate pages based on content 
}

?>