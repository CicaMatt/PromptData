<?php

// Use of nullable return types
function getMyObject(): ?MyObject {
    // ... your logic here
    if ($someCondition) {
        return new MyObject();
    } else {
        return null;
    }
}

// Usage
$myObject = getMyObject();

if ($myObject === null) {
    // Handle the case where no object is returned
} else {
    // Work with the returned object
}