<?php

class MyObject
{
    // ... properties and methods of MyObject ...
}

function getMyObject(): ?MyObject
{
    // Initialize variables
    $objectFound = false;
    $myObject = null;

    // ... logic to retrieve or create a MyObject ...

    if ($objectFound) {
        return $myObject; // Return the found or created MyObject
    } else {
        return null; // Explicitly indicate that no object was found
    }
}

$myObject = getMyObject();

if ($myObject !== null) {
    // Object found, proceed with operations on $myObject
    $myObject->someMethod(); 
} else {
    // Object not found, handle the null case gracefully
    echo "No object found."; 
}
