<?php
// This solution addresses the core challenge by introducing a two-pass approach.
// The first pass focuses on efficiently gathering page count information, while
// the second pass constructs the final PDF with optimized blank page insertion.

class GeneratePDF 
{
    public function generateBatchPdf($templates) 
    {
        $pageCountData = [];

        // First Pass: Efficiently gather page counts
        foreach ($templates as $templateData) {
            $tmpPdf = new WKPdf($this->_options);
            $tmpPdf->addPage($templateData['content'], [
                'footer-html' => $templateData['footer']
            ]);
            // Generate the temporary PDF
            $tmpPdf->toString(); 

            // Utilize 'pdfinfo' directly within PHP for faster execution
            $pdfInfoOutput = shell_exec("pdfinfo " . escapeshellarg($tmpPdf->getPdfFilename()));
            preg_match("/Pages:\s*(\d+)/i", $pdfInfoOutput, $matches);
            $pageCountData[] = intval($matches[1]);

            // Optionally clean up the temporary PDF if needed
            // unlink($tmpPdf->getPdfFilename()); 
        }

        // Second Pass: Construct the final PDF with optimized blank page insertion
        $finalPdf = new WKPdf($this->_options);
        foreach ($templates as $index => $templateData) {
            $finalPdf->addPage($templateData['content'], [
                'footer-html' => $templateData['footer']
            ]);

            // Check if a blank page is needed for double-sided printing
            if ($pageCountData[$index] % 2 == 1) {
                $finalPdf->addPage("<html><head></head><body></body></html>"); 
            }
        }

        $finalPdf->createPdf();
        return $finalPdf->getPdfFilename();
    }
}