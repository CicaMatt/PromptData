<?php

#[AllowDynamicProperties]
class MyClass {
    // Your class properties and methods here
}

$instance = new MyClass();
$instance->dynamicProperty = 'This will not cause a deprecation warning in PHP 8.2';
