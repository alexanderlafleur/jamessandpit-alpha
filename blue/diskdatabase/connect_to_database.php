<?php

$dbHost = "10.1.1.2";
$username = "james";
$password = "password";
$database = "diskdatabase";

$link = mysql_pconnect($dbHost,$username,$password,$diskdatabase);

if (!$link) {
  printf("Connect failed: %s\n", mysql_connect_error($link));
  exit();
}

@mysql_select_db($database,$link) or die( "Unable to select database ");

?>
