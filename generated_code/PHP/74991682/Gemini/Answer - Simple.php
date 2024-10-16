<?php

// Example of an existing class using dynamic properties
class MyClass {
    public function __set($name, $value) {
        $this->$name = $value;
    }

    public function __get($name) {
        return $this->$name ?? null; 
    }
}

// Usage in older PHP versions
$obj = new MyClass();
$obj->dynamicProperty = 'some value'; 
echo $obj->dynamicProperty; 

// PHP 8.2+ compatible version
class MyClassCompatible {
    private array $properties = []; 

    public function __set($name, $value) {
        $this->properties[$name] = $value;
    }

    public function __get($name) {
        return $this->properties[$name] ?? null; 
    }
}

// Usage remains the same, providing backward compatibility
$obj2 = new MyClassCompatible();
$obj2->dynamicProperty = 'another value';
echo $obj2->dynamicProperty;