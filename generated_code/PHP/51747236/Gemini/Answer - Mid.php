<?php
function getNumPages($complete = false)
{
    if (!$complete) {
        $tmpPdf = new WKPdf($this->_options);
        $tmpPdf->addPage($this->content, ['footer-html' => $this->footer]);
        $tmpPdf->toString(); 
        $document = $tmpPdf->getPdfFilename();
    } else {
        $this->createPdf();
        $document = $this->getPdfFilename();
    }

    $cmd = 'pdfinfo ' . escapeshellarg($document); // Sanitize input
    exec($cmd, $output, $returnVar);

    if ($returnVar !== 0) {
        // Handle pdfinfo error
        throw new Exception("Error getting page count from pdfinfo");
    }

    $pagecount = 0;
    foreach ($output as $op) {
        if (preg_match("/Pages:\s*(\d+)/i", $op, $matches) === 1) {
            $pagecount = intval($matches[1]);
            break;
        }
    }

    return $pagecount;
}