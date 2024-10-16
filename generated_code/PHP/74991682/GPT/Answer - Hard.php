<?php

// PHP 8.2+ compatible class with dynamic properties handling
class MyClass
{
    // Store undefined properties
    private array $dynamicProperties = [];

    // Magic method to handle setting dynamic properties
    public function __set(string $name, $value): void
    {
        $this->dynamicProperties[$name] = $value;
    }

    // Magic method to handle getting dynamic properties
    public function __get(string $name)
    {
        return $this->dynamicProperties[$name] ?? null;
    }

    // Magic method to check if a dynamic property is set
    public function __isset(string $name): bool
    {
        return isset($this->dynamicProperties[$name]);
    }

    // Magic method to unset a dynamic property
    public function __unset(string $name): void
    {
        unset($this->dynamicProperties[$name]);
    }
}

$instance = new MyClass();
$instance->someDynamicProperty = 'Dynamic Property Value'; // This works without issues
echo $instance->someDynamicProperty; // Outputs: Dynamic Property Value

