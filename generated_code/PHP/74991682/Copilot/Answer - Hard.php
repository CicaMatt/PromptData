<?php
class MyClass {
    private array $properties = [];

    public function __get(string $name) {
        return $this->properties[$name] ?? null;
    }

    public function __set(string $name, $value): void {
        $this->properties[$name] = $value;
    }
}

// Usage
$obj = new MyClass();
$obj->dynamicProperty = 'value'; 
echo $obj->dynamicProperty; 
