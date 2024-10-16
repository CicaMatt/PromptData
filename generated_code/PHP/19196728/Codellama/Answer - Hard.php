<?php
class MCrypt {
  // Same as in JAVA         
  private $iv = '0000000000000000';      
  private $key = 'U1MjU1M0FDOUZ.Qz'; 
  
  function __construct() 
  {
    $this->key = hash('sha256', $this->key, true);
  }
  
  function encrypt($str) 
  {
    $iv = openssl_random_pseudo_bytes(16);
    $td = mcrypt_module_open(MCRYPT_RIJNDAEL_128, '', MCRYPT_MODE_CBC, '');
    mcrypt_generic_init($td, $this->key, $iv);
    $block = mcrypt_get_block_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
    $pad = $block - (strlen($str) % $block);
    $str .= str_repeat(chr($pad), $pad);
    $encrypted = mcrypt_generic($td, $str);
    mcrypt_generic_deinit($td);
    mcrypt_module_close($td);
    return base64_encode($encrypted . $iv);
  }
  
  function decrypt($code) 
  {
    $iv = substr($code, -16);
    $td = mcrypt_module_open('rijndael-128', '', 'cbc', '');
    mcrypt_generic_init($td, $this->key, $iv);
    $str = mdecrypt_generic($td, base64_decode($code));
    $block = mcrypt_get_block_size('rijndael-128', 'cbc');
    mcrypt_generic_deinit($td);
    mcrypt_module_close($td);
    return $this->strippadding($str);             
  }
  
  /*
    For PKCS7 padding
   */
  private function addpadding($string, $blocksize = 16) 
  {
    $len = strlen($string);
    $pad = $blocksize - ($len % $blocksize);
    $string .= str_repeat(chr($pad), $pad);
    return $string;
  }
  
  private function strippadding($string) 
  {
    $slast = ord(substr($string, -1));
    $slastc = chr($slast);
    $pcheck = substr($string, -$slast);
    if (preg_match("/$slastc{" . $slast . "}/", $string)) {
      $string = substr($string, 0, strlen($string) - $slast);
      return $string;
    } else {
      return false;
    }
  }
}