<?php

class MyObject
{
    public function doSomething()
    {
        echo "Doing something!";
    }
}

class MyService
{
    public function getMyObject() : ?MyObject
    {
        // Simulate some logic that might return null
        $condition = rand(0, 1);
        if ($condition === 1) {
            return new MyObject();
        }
        return null;
    }
}

$service = new MyService();
$myObject = $service->getMyObject();

if ($myObject !== null) {
    $myObject->doSomething();
} else {
    echo "No object returned.";
}
