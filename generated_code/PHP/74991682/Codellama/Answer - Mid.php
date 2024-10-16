<?php
// Use the magic method __get() to handle dynamic property access
class MyClass {
    public $foo;
    
    public function __get($name) {
        if (property_exists($this, $name)) {
            return $this->$name;
        }
        
        // Handle dynamic properties with the deprecated creation of dynamic property warning
        trigger_error(sprintf('Creation of dynamic property %s is deprecated', $name), E_DEPRECATED);
        return null;
    }
}