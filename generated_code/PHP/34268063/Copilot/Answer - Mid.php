<?php
class MyObject 
{
    // Class implementation
}

class MyClass 
{
    public function getMyObject(): ?MyObject 
    {
        // Your logic here
        return null; 
    }
}

$instance = new MyClass();
$result = $instance->getMyObject();

if ($result === null) 
{
    // Handle the null case
} else {
    // Use the MyObject instance
}
