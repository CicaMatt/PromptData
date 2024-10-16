<?php
class MyObject 
{
    // Class implementation
}

class MyService 
{
    public function getMyObject(): ?MyObject 
    {
        // Your logic here
        return null; 
    }
}

$service = new MyService();
$myObject = $service->getMyObject();

if ($myObject === null) 
{
    // Handle the case where the object is null
    echo "Object is null";
} else {
    // Proceed with the object
    echo "Object is not null";
}
