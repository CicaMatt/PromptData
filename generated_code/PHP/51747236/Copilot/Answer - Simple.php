<?php
require_once('vendor/autoload.php');

use setasign\Fpdi\Fpdi;

function addBlankPages($inputFiles, $outputFile) {
    $pdf = new Fpdi();

    foreach ($inputFiles as $file) {
        $pageCount = $pdf->setSourceFile($file);
        for ($i = 1; $i <= $pageCount; $i++) {
            $tplIdx = $pdf->importPage($i);
            $pdf->AddPage();
            $pdf->useTemplate($tplIdx);

            // Check if the current page is the last page and if it's odd
            if ($i == $pageCount && $pageCount % 2 != 0) {
                $pdf->AddPage(); // Add a blank page
            }
        }
    }

    $pdf->Output($outputFile, 'F');
}

$inputFiles = ['invoice1.pdf', 'invoice2.pdf', 'invoice3.pdf'];
$outputFile = 'combined_invoices.pdf';
addBlankPages($inputFiles, $outputFile);
