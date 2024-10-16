<?php
class MyClass {
    private $dynamicProperty;
  
    public function __get($name) {
        if (property_exists($this, $name)) {
            return $this->$name;
        }
        throw new \Exception('Unknown property "'.$name.'"');
    }
  
    public function __set($name, $value) {
        if (property_exists($this, $name)) {
            $this->$name = $value;
        } else {
            throw new \Exception('Unknown property "'.$name.'"');
        }
    }
}