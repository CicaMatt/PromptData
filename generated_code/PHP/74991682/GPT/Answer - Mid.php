<?php
class DynamicPropertiesHandler {
    private array $properties = [];

    // Magic method to get dynamic property
    public function __get($name) {
        return $this->properties[$name] ?? null;
    }

    // Magic method to set dynamic property
    public function __set($name, $value) {
        $this->properties[$name] = $value;
    }

    // Magic method to check if a dynamic property is set
    public function __isset($name) {
        return isset($this->properties[$name]);
    }

    // Magic method to unset a dynamic property
    public function __unset($name) {
        unset($this->properties[$name]);
    }
}

// Example usage
$obj = new DynamicPropertiesHandler();
$obj->newProperty = 'This is a dynamic property';
echo $obj->newProperty; // Outputs: This is a dynamic property
