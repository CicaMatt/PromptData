<?php
class MyClass {
    private $myProperty;

    public function __construct() {
        $this->myProperty = 'initial value';
    }

    public function getMyProperty(): string {
        return $this->myProperty;
    }

    public function setMyProperty(string $value) {
        $this->myProperty = $value;
    }
}