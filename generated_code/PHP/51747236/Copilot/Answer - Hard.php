<?php

// Custom Exceptions
class PdfGenerationException extends RuntimeException {}
class PdfProcessException extends PdfGenerationException {}
class PdfPageCountException extends PdfGenerationException {}

class InvoiceBatchGenerator
{
    private $_options;
    private $invoices = [];
    private $footer;

    public function __construct($options, $footer)
    {
        $this->_options = $options;
        $this->footer = $footer;
    }

    public function addInvoice($content)
    {
        $this->invoices[] = $content;
    }

    public function generateBatchPdf()
    {
        $batchPdf = new WKPdf($this->_options);
        foreach ($this->invoices as $invoice) {
            $numPages = $this->getNumPages($invoice);
            $batchPdf->addPage($invoice, ['footer-html' => $this->footer]);

            // Add a blank page if the number of pages is odd
            if ($numPages % 2 !== 0) {
                $batchPdf->addPage('<html><body></body></html>');
            }
        }

        return $batchPdf->toString();
    }

    private function getNumPages($content)
    {
        $tmpPdf = new WKPdf($this->_options);
        $tmpPdf->addPage($content, ['footer-html' => $this->footer]);
        $pdfString = $tmpPdf->toString();

        $pageCount = $this->countPages($pdfString);
        return $pageCount;
    }

    private function countPages($pdfString)
    {
        $cmd = 'pdfinfo';
        $descriptorspec = [
            0 => ["pipe", "r"],  // stdin
            1 => ["pipe", "w"],  // stdout
            2 => ["pipe", "w"]   // stderr
        ];

        $process = proc_open($cmd, $descriptorspec, $pipes);
        if (is_resource($process)) {
            fwrite($pipes[0], $pdfString);
            fclose($pipes[0]);

            $output = stream_get_contents($pipes[1]);
            fclose($pipes[1]);

            $errors = stream_get_contents($pipes[2]);
            fclose($pipes[2]);

            $return_value = proc_close($process);

            if ($return_value !== 0) {
                throw new PdfProcessException("Error counting pages: $errors");
            }

            if (preg_match("/Pages:\s*(\d+)/i", $output, $matches) === 1) {
                return intval($matches[1]);
            } else {
                throw new PdfPageCountException("Unable to determine page count from output.");
            }
        } else {
            throw new PdfProcessException("Unable to open process for pdfinfo.");
        }
    }
}

// Usage example
$options = ['no-outline', 'margin-top' => 10, 'margin-right' => 10, 'margin-bottom' => 10, 'margin-left' => 10];
$footer = '<html><body>Footer content here</body></html>';

$generator = new InvoiceBatchGenerator($options, $footer);
$generator->addInvoice('<html><body>Invoice 1 content here</body></html>');
$generator->addInvoice('<html><body>Invoice 2 content here</body></html>');
// Add more invoices as needed

try {
    $batchPdf = $generator->generateBatchPdf();
    file_put_contents('batch_invoices.pdf', $batchPdf);
} catch (PdfGenerationException $e) {
    echo "An error occurred during PDF generation: " . $e->getMessage();
}
?>
