<?php

class MyObject {
    // Class implementation
}

class MyService {
    private $myObject;

    public function __construct() {
        // Initialize $myObject or leave it as null
        $this->myObject = null; // or new MyObject();
    }

    public function getMyObject(): ?MyObject {
        return $this->myObject;
    }
}

$service = new MyService();
$myObject = $service->getMyObject();

if ($myObject !== null) {
    // Use $myObject
    echo "Object is available.";
} else {
    // Handle the case where $myObject is null
    echo "Object is not available.";
}
?>
