<?php
#[\AllowDynamicProperties]
class MyClass {
    public $existingProperty;

    public function __construct() {
        $this->existingProperty = 'value';
    }
}

$obj = new MyClass();
$obj->dynamicProperty = 'dynamicValue'; // This will not throw an error in PHP 8.2 or later.
