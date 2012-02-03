<?php
  session_start();
  header('Content-Type: text/event-stream');

  if(array_key_exists('message', $_POST)) {
    $_SESSION['logoot_message'] = $_POST['message'];
  }

  echo 'data: ' . $_SESSION['logoot_message'];
