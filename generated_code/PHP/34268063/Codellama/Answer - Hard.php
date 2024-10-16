<?php
class MyObject 
{
  private $name;

  public function setName($name) 
  {
    $this->name = $name;
  }

  // ... other methods here ...
}

class MyObjectManager 
{
  private $nullObject;

  public function __construct() 
  {
    $this->nullObject = new MyObject();
    $this->nullObject->setName('Null Object');
  }

  private function objectFound() 
  {
    // Logic to determine if the object is found
    return false;
  }

  public function getMyObject() : MyObject
  {
    if (!$this->objectFound()) {
      return $this->nullObject;
    }

    return new MyObject();
  }
}
