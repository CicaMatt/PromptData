<?php

class PdfGenerationException extends RuntimeException {}
class PdfInfoException extends RuntimeException {}
class PdfCombinationException extends RuntimeException {}

class InvoiceBatchPdfGenerator
{
    private $wkhtmltopdfPath = '/usr/local/bin/wkhtmltopdf'; // Path to wkhtmltopdf binary
    private $pdfinfoPath = '/usr/bin/pdfinfo'; // Path to pdfinfo binary
    private $invoices = [];
    private $batchOutputFile;

    public function __construct($outputFile)
    {
        if (!is_writable(dirname($outputFile))) {
            throw new PdfGenerationException("The output directory is not writable.");
        }

        $this->batchOutputFile = $outputFile;
    }

    // Add an invoice template to the batch
    public function addInvoice($htmlContent, $footerHtml = null)
    {
        if (empty($htmlContent)) {
            throw new InvalidArgumentException("HTML content for the invoice cannot be empty.");
        }

        $this->invoices[] = [
            'html' => $htmlContent,
            'footer' => $footerHtml
        ];
    }

    // Generate the final batch PDF
    public function generateBatchPdf()
    {
        if (empty($this->invoices)) {
            throw new PdfGenerationException("No invoices have been added to the batch.");
        }

        $allPdfs = [];

        foreach ($this->invoices as $invoice) {
            // Generate PDF for each invoice and check the page count
            $invoicePdf = $this->generateInvoicePdf($invoice['html'], $invoice['footer']);
            $numPages = $this->getPdfPageCount($invoicePdf);

            // If the invoice has an odd number of pages, append a blank page
            if ($numPages % 2 !== 0) {
                $invoicePdf = $this->appendBlankPage($invoicePdf);
            }

            // Store the final PDF (with or without the blank page)
            $allPdfs[] = $invoicePdf;
        }

        // Combine all PDFs into a single file
        $this->combinePdfs($allPdfs, $this->batchOutputFile);
    }

    // Generate a single PDF for an invoice
    private function generateInvoicePdf($htmlContent, $footerHtml = null)
    {
        $tmpFile = tempnam(sys_get_temp_dir(), 'invoice_') . '.pdf';
        $cmd = $this->wkhtmltopdfPath . " --quiet --footer-html '$footerHtml' - - > $tmpFile";

        // Execute wkhtmltopdf and generate PDF
        $descriptorSpec = [
            0 => ["pipe", "r"], // stdin for HTML content
            1 => ["pipe", "w"], // stdout
            2 => ["pipe", "w"]  // stderr
        ];

        $process = proc_open($cmd, $descriptorSpec, $pipes);

        if (is_resource($process)) {
            fwrite($pipes[0], $htmlContent);
            fclose($pipes[0]);

            // Read and ignore stdout
            fclose($pipes[1]);

            // Capture any errors
            $stderr = stream_get_contents($pipes[2]);
            fclose($pipes[2]);

            $returnCode = proc_close($process);

            if (!empty($stderr) || $returnCode !== 0) {
                throw new PdfGenerationException("Failed to generate PDF: " . $stderr);
            }

            if (!file_exists($tmpFile)) {
                throw new PdfGenerationException("The generated PDF file could not be found.");
            }

            return $tmpFile;
        }

        throw new PdfGenerationException("Failed to start the wkhtmltopdf process.");
    }

    // Get the number of pages in a PDF using pdfinfo
    private function getPdfPageCount($pdfFile)
    {
        if (!file_exists($pdfFile)) {
            throw new PdfInfoException("PDF file does not exist: " . $pdfFile);
        }

        $output = [];
        $cmd = escapeshellarg($this->pdfinfoPath) . ' ' . escapeshellarg($pdfFile);
        exec($cmd, $output, $returnCode);

        if ($returnCode !== 0) {
            throw new PdfInfoException("Failed to retrieve PDF information. Command execution error.");
        }

        foreach ($output as $line) {
            if (preg_match('/Pages:\s+(\d+)/', $line, $matches)) {
                return (int)$matches[1];
            }
        }

        throw new PdfInfoException("Unable to determine the number of pages in the PDF.");
    }

    // Append a blank page to a PDF
    private function appendBlankPage($pdfFile)
    {
        if (!file_exists($pdfFile)) {
            throw new PdfCombinationException("PDF file does not exist: " . $pdfFile);
        }

        $tmpFile = tempnam(sys_get_temp_dir(), 'invoice_') . '.pdf';
        $cmd = "pdftk " . escapeshellarg($pdfFile) . " output " . escapeshellarg($tmpFile) . " pad 2";
        exec($cmd, $output, $returnCode);

        if ($returnCode !== 0) {
            throw new PdfCombinationException("Failed to append a blank page. Command execution error.");
        }

        return $tmpFile;
    }

    // Combine multiple PDFs into one
    private function combinePdfs($pdfFiles, $outputFile)
    {
        foreach ($pdfFiles as $file) {
            if (!file_exists($file)) {
                throw new PdfCombinationException("Cannot combine non-existing PDF file: " . $file);
            }
        }

        $fileList = implode(' ', array_map('escapeshellarg', $pdfFiles));
        $cmd = "pdftk $fileList cat output " . escapeshellarg($outputFile);
        exec($cmd, $output, $returnCode);

        if ($returnCode !== 0) {
            throw new PdfCombinationException("Failed to combine PDFs into a single file. Command execution error.");
        }

        if (!file_exists($outputFile)) {
            throw new PdfCombinationException("The combined PDF file could not be created.");
        }
    }
}

// Usage example
try {
    $batchPdf = new InvoiceBatchPdfGenerator('batch_output.pdf');

    // Add multiple invoices (HTML content and optional footer)
    $batchPdf->addInvoice('<html>Your invoice content 1</html>', '<footer>Your footer content 1</footer>');
    $batchPdf->addInvoice('<html>Your invoice content 2</html>', '<footer>Your footer content 2</footer>');
    // Add more invoices as needed...

    // Generate the final batch PDF
    $batchPdf->generateBatchPdf();

    echo "Batch PDF generated successfully!";
} catch (PdfGenerationException $e) {
    echo "Error generating PDF: " . $e->getMessage();
} catch (PdfInfoException $e) {
    echo "Error retrieving PDF info: " . $e->getMessage();
} catch (PdfCombinationException $e) {
    echo "Error combining PDFs: " . $e->getMessage();
} catch (Exception $e) {
    // Catch any other unexpected exceptions
    echo "An unexpected error occurred: " . $e->getMessage();
}
