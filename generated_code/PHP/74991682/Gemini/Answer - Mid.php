<?php
class MyClass {
    private array $dynamicProperties = [];

    public function __set($name, $value) {
        $this->dynamicProperties[$name] = $value;
    }

    public function __get($name) {
        if (array_key_exists($name, $this->dynamicProperties)) {
            return $this->dynamicProperties[$name];
        } else {
            trigger_error('Undefined property via __get(): ' . $name, E_USER_NOTICE);
            return null;
        }
    }

    public function __isset($name) {
        return isset($this->dynamicProperties[$name]);
    }

    public function __unset($name) {
        unset($this->dynamicProperties[$name]);
    }
}