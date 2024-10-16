<?php

// Encryption function
function encrypt($plainText) 
{
  $passPhrase = "passpharse";
  $saltValue = "saltvalue";
  $hashAlgorithm = "SHA1";
  $passwordIterations = 2;
  $initVector = "1a2b3c4d5e6f7g8h";
  $keySize = 256;
  
  // Convert plain text to byte array
  $plainTextBytes = mb_convert_encoding($plainText, "UTF-8");
  
  // Generate password salt and hashed password using PBKDF2
  $saltValueBytes = mb_convert_encoding($saltValue, "ASCII");
  $passwordHash = hash_pbkdf2($hashAlgorithm, $passPhrase, $saltValueBytes, $passwordIterations, $keySize);
  
  // Generate initialization vector using PBKDF2
  $initVectorBytes = mb_convert_encoding($initVector, "ASCII");
  
  // Create RijndaelManaged symmetric key using generated password and salt
  $rijndaelManaged = new RijndaelManaged(0, $passwordHash);
  $rijndaelManaged->Mode = CipherMode::CBC;
  
  // Encrypt plain text using RijndaelManaged symmetric key and initialization vector
  $cipherText = mcrypt_encrypt($rijndaelManaged, $plainTextBytes, $initVectorBytes);
  
  // Convert cipher text to base64 string
  $base64CipherText = base64_encode($cipherText);
  
  return $base64CipherText;
}

// Decryption function
function decrypt($cipherText) 
{
  $passPhrase = "passpharse";
  $saltValue = "saltvalue";
  $hashAlgorithm = "SHA1";
  $passwordIterations = 2;
  $initVector = "1a2b3c4d5e6f7g8h";
  $keySize = 256;
  
  // Convert cipher text from base64 string to byte array
  $cipherTextBytes = base64_decode($cipherText);
  
  // Generate password salt and hashed password using PBKDF2
  $saltValueBytes = mb_convert_encoding($saltValue, "ASCII");
  $passwordHash = hash_pbkdf2($hashAlgorithm, $passPhrase, $saltValueBytes, $passwordIterations, $keySize);
  
  // Generate initialization vector using PBKDF2
  $initVectorBytes = mb_convert_encoding($initVector, "ASCII");
  
  // Create RijndaelManaged symmetric key using generated password and salt
  $rijndaelManaged = new RijndaelManaged(0, $passwordHash);
  $rijndaelManaged->Mode = CipherMode::CBC;
  
  // Decrypt cipher text using RijndaelManaged symmetric key and initialization vector
  $plainTextBytes = mcrypt_decrypt($rijndaelManaged, $cipherTextBytes, $initVectorBytes);
  
  // Convert plain text to UTF-8 string
  $plainText = mb_convert_encoding($plainTextBytes, "UTF-8");
  
  return $plainText;
}