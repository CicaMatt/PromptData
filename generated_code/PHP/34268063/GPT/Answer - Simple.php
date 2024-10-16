<?php
class MyObject
{
    // Your class implementation here
}

class MyClass
{
    public function getMyObject(): ?MyObject
    {
        // Your logic here
        // Return null if no object is available
        return null;
    }
}

$myClass = new MyClass();
$object = $myClass->getMyObject();

if ($object === null) {
    // Handle the case when null is returned
    echo "No object returned.";
} else {
    // Use the object
    echo "Object returned.";
}
