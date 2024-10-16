<?php
function getMyObject() : ?MyObject
{
    return null;
}

try {
    $myObject = $this->getMyObject();
    if ($myObject) {
        // Use the object safely
    } else {
        // Handle the case where the object is null
    }
} catch (Exception $e) {
    // Catch any exceptions thrown by the method
}