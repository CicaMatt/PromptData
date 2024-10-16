<?php
try {
  $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOC_ROOT'].'/uploads/'.$_FILES['file']['name']);
} catch (Exception $e) {
  // The move_uploaded_file() function may throw an exception if the file could not be uploaded for some reason.
  echo 'Error: ' . $e->getMessage();
} finally {
  if ($move) {
      //opens the uploaded file for extraction
      echo 'Upload Complete!';
  } else {
      die ('File didn\'t upload');
  }
}
?>
