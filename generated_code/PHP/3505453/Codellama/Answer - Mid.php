<?php
class Encryption {
    private $passphrase;
    private $saltValue;
    private $hashAlgorithm;
    private $passwordIterations;
    private $initVector;
    private $keySize;

    function __construct($passphrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
        $this->passphrase = $passphrase;
        $this->saltValue = $saltValue;
        $this->hashAlgorithm = $hashAlgorithm;
        $this->passwordIterations = $passwordIterations;
        $this->initVector = $initVector;
        $this->keySize = $keySize;
    }

    function encrypt($plainText) {
        $plainTextBytes = mb_convert_encoding($plainText, "UTF-8");
        $saltValueBytes = mb_convert_encoding($this->saltValue, "UTF-8");
        $initVectorBytes = mb_convert_encoding($this->initVector, "ASCII");

        $passwordDeriveBytes = new PasswordDeriveBytes(
            $this->passphrase,
            $saltValueBytes,
            $this->hashAlgorithm,
            $this->passwordIterations);

        $keyBytes = $passwordDeriveBytes->GetBytes($this->keySize / 8);

        $rijndaelManaged = new RijndaelManaged();
        $rijndaelManaged->Mode = CipherMode::CBC;

        $encryptor = $rijndaelManaged->CreateEncryptor($keyBytes, $initVectorBytes);
        $memoryStream = new MemoryStream();
        $cryptoStream = new CryptoStream($memoryStream, $encryptor, CryptoStreamMode::Write);
        $cryptoStream->Write($plainTextBytes, 0, strlen($plainTextBytes));

        $cryptoStream->FlushFinalBlock();

        $cipherTextBytes = $memoryStream->ToArray();
        $cipherText = base64_encode($cipherTextBytes);

        return $cipherText;
    }
}