<?php

class MyObject 
{
    // Class implementation
}

class MyClass 
{
    // Return type is MyObject or null
    public function getMyObject(): ?MyObject 
    {
        // Return either an instance of MyObject or null
        return null;  
    }
}

$myClass = new MyClass();
$result = $myClass->getMyObject();

if ($result === null) {
    // Handle the null case
    echo "No object returned";
} else {
    // Use the returned object
    echo "Object returned";
}
