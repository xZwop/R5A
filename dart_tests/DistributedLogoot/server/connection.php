<?php
  session_start();
  header('Content-type: text/html; charset=UTF-8');

  $_SESSION['logoot_message'] = '';
  echo 'id' . rand(42, 1337);
