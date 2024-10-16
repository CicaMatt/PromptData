<?php

class InvoiceBatchGenerator
{
    private $invoices = [];
    private $wkhtmltopdfPath = '/path/to/wkhtmltopdf';

    public function addInvoice($htmlContent)
    {
        $this->invoices[] = $htmlContent;
    }

    public function generateBatchPdf($outputFile)
    {
        $combinedHtml = $this->combineInvoices();
        $this->generatePdf($combinedHtml, $outputFile);
    }

    private function combineInvoices()
    {
        $combinedHtml = '<html><body>';
        foreach ($this->invoices as $invoice) {
            $combinedHtml .= $invoice;
            if ($this->isOddPage($invoice)) {
                $combinedHtml .= '<div style="page-break-after: always;"></div>';
            }
        }
        $combinedHtml .= '</body></html>';
        return $combinedHtml;
    }

    private function isOddPage($htmlContent)
    {
        $tmpFile = tempnam(sys_get_temp_dir(), 'invoice');
        file_put_contents($tmpFile, $htmlContent);
        $pageCount = $this->getPageCount($tmpFile);
        unlink($tmpFile);
        return $pageCount % 2 !== 0;
    }

    private function getPageCount($pdfFile)
    {
        $cmd = "pdfinfo \"$pdfFile\"";
        exec($cmd, $output);
        foreach ($output as $line) {
            if (preg_match('/Pages:\s*(\d+)/i', $line, $matches)) {
                return (int)$matches[1];
            }
        }
        return 0;
    }

    private function generatePdf($htmlContent, $outputFile)
    {
        $tmpHtmlFile = tempnam(sys_get_temp_dir(), 'batch');
        file_put_contents($tmpHtmlFile, $htmlContent);
        $cmd = "{$this->wkhtmltopdfPath} \"$tmpHtmlFile\" \"$outputFile\"";
        exec($cmd);
        unlink($tmpHtmlFile);
    }
}

// Usage example
$generator = new InvoiceBatchGenerator();
$generator->addInvoice('<html><body>Invoice 1 content</body></html>');
$generator->addInvoice('<html><body>Invoice 2 content</body></html>');
// Add more invoices as needed
$generator->generateBatchPdf('/path/to/output/batch.pdf');
