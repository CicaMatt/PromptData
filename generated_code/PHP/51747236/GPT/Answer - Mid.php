<?php

class PDFBatchGenerator {

    private $_options;
    private $content = [];  // Store the content of multiple invoices
    private $footer;        // Common footer template

    public function __construct($options) {
        $this->_options = $options;
    }

    // Add an invoice to the batch
    public function addInvoice($htmlContent, $footerHtml = '') {
        $this->content[] = [
            'html' => $htmlContent,
            'footer' => $footerHtml
        ];
    }

    // Generate the full PDF with blank pages for double-sided printing
    public function generateBatchWithBlankPages($outputFilename) {
        // 1. Create the initial PDF without blank pages
        $tmpFilename = '/tmp/batch_tmp_' . time() . '.pdf';
        $this->generateInitialPDF($tmpFilename);

        // 2. Use pdfinfo to determine the page count of the initial PDF
        $pagesPerInvoice = $this->getInvoicePageCounts($tmpFilename);

        // 3. Insert blank pages if needed for double-sided printing
        $this->generateFinalPDFWithBlanks($pagesPerInvoice, $outputFilename);

        // Clean up temporary file
        unlink($tmpFilename);
    }

    // Generate the initial PDF for all invoices combined
    private function generateInitialPDF($filename) {
        $wkpdf = new WKPdf($this->_options);

        foreach ($this->content as $invoice) {
            $wkpdf->addPage($invoice['html'], ['footer-html' => $invoice['footer']]);
        }

        $wkpdf->saveAs($filename);
    }

    // Get the number of pages for each invoice using pdfinfo
    private function getInvoicePageCounts($pdfFile) {
        $cmd = 'pdfinfo ' . escapeshellarg($pdfFile);
        exec($cmd, $output);

        $pageCount = 0;
        $pagesPerInvoice = [];

        foreach ($output as $op) {
            if (preg_match("/Pages:\s*(\d+)/i", $op, $matches) === 1) {
                $totalPages = intval($matches[1]);
                $pagesPerInvoice[] = $totalPages - $pageCount;  // Track pages per invoice
                $pageCount = $totalPages;
            }
        }

        return $pagesPerInvoice;
    }

    // Generate the final PDF with blank pages where necessary
    private function generateFinalPDFWithBlanks($pagesPerInvoice, $outputFilename) {
        $wkpdf = new WKPdf($this->_options);

        foreach ($this->content as $index => $invoice) {
            $wkpdf->addPage($invoice['html'], ['footer-html' => $invoice['footer']]);

            // Add a blank page if the invoice has an odd number of pages
            if ($pagesPerInvoice[$index] % 2 !== 0) {
                $wkpdf->addPage('<html><body style="page-break-before: always;"></body></html>');
            }
        }

        // Save the final PDF with blank pages
        $wkpdf->saveAs($outputFilename);
    }
}

// Usage Example:

$options = ['orientation' => 'portrait'];  // wkhtmltopdf options
$pdfBatch = new PDFBatchGenerator($options);

// Add multiple invoices to the batch
$pdfBatch->addInvoice('<html><body>Invoice 1</body></html>', '<footer>Footer 1</footer>');
$pdfBatch->addInvoice('<html><body>Invoice 2</body></html>', '<footer>Footer 2</footer>');
// Add more invoices...

// Generate the PDF with blank pages for double-sided printing
$pdfBatch->generateBatchWithBlankPages('/path/to/output/final_batch.pdf');

?>
