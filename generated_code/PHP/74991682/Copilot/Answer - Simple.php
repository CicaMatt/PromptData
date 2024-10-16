<?php

#[AllowDynamicProperties]
class MyClass {
    // Your class implementation
}

$instance = new MyClass();
$instance->dynamicProperty = 'value'; // No deprecation warning
