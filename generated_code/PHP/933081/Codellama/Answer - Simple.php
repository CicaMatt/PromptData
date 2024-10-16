<?php
try {
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);
  } catch (Exception $e) {
      echo 'Error moving file.';
  } finally {
    if ($move) {
        //opens the uploaded file for extraction
        echo 'Upload Complete!';
    } else {
        die('File didn\'t upload');
    }
  }