
<?php

function printAllWallets() {
  $query = "SELECT id, description FROM wallet order by description";

  $result=mysql_query($query, $link);

  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  $wallets = mysql_fetch_assoc($result);

  while ($wallet = mysql_fetch_assoc($result)) {
    echo 
      "<li class=\"browse-list-item\"> " . 
      "<a href=\"browse.html?method=wallet&id=" . $wallet["id"] . "\"/>" .
      $wallet["description"] . 
      "</a> " .
      "</li> ";
  }
}

function printWallet($walletId) {

  $query = sprintf("SELECT id, description FROM wallet where id = '%s'", mysql_escape_string($walletId));
     
  $result = mysql_query($query, $link);
     
  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };
     
  while ($wallet = mysql_fetch_assoc($result)) {
    echo "<span class=\"browse-selected-item\">" .  $wallet["description"] . "</span> <br/>";
    
    printDisks($walletId);
  }
}

function printDisk($diskId) {
  
  $query = sprintf("SELECT id, label FROM disk where id = '%s'", mysql_escape_string($diskId));
  
  $result = mysql_query($query, $link);
  
  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  while ($disk = mysql_fetch_assoc($result)) {
    echo "<span class=\"browse-selected-item\">" .  $disk["label"] . "</span> <br/>";
    
    printDirs($diskId);
  }
}


function printDir($dirId) {
  
  $query = sprintf("SELECT id, name, parent FROM dir where id = '%s'", mysql_escape_string($dirId));
  
  $result = mysql_query($query, $link);
  
  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  while ($dir = mysql_fetch_assoc($result)) {
    echo "<span class=\"browse-selected-item\">" .  $dir["name"] . "</span> <br/>";
    
    printFiles($diskId);

    printMp3s($diskId);
  }
}


function printFile($fileId) {
  
  $query = sprintf("SELECT id, name, parent,size, type, path FROM diskfile where id = '%s'", mysql_escape_string($fileId));
  
  $result = mysql_query($query, $link);
  
  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  while ($file = mysql_fetch_assoc($result)) {
    echo "<span class=\"browse-selected-item\">" .  $file["name"] . "</span> " . $file["path"] . " <br/>";
  }
}

function printMp3($mp3Id) {
  
  $query = sprintf("SELECT id, name, parent,size, type, path FROM diskfile where id = '%s'", mysql_escape_string($fileId));
  
  $result = mysql_query($query, $link);
  
  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  while ($file = mysql_fetch_assoc($result)) {
    echo "<span class=\"browse-selected-item\">" .  $file["name"] . "</span> " . $file["path"] . " <br/>";
  }
}

function printDisks($walletId) {

  echo "<h2>Disks </h2>";
  $query = sprintf("SELECT id, label, medianumber FROM disk where wallet_id = %s order by label", mysql_escape_string($walletId));

  $result = mysql_query($query, $link);

  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  while ($disk = mysql_fetch_assoc($result)) {
     echo
       "<li class=\"browse-list-item\"> " .
       "<a href=\"browse.html?method=disk&id=" . $disk["id"] . "\"/>" .
       $disk["label"] .
       "</a> " .
       "</li> ";
  }

  mysql_free_result($result);
  mysql_close($link);

  return $disks;

}

function printAllWallets()
{
  include("connect_to_database.php");

  $query = "SELECT id, description FROM wallet order by description";

  $result=mysql_query($query, $link);

  if (!$result) {
    die("Invalid query: " . mysql_error($link));
  };

  $wallets = mysql_fetch_assoc($result);

  mysql_free_result($result);
  mysql_close();

  return $wallets;
  
}

function searchForWallets($query)
{

   include("connect_to_database.php");
   
   $query = "SELECT id, description FROM wallet WHERE description like \"%". mysql_real_escape_string($query). "%\"";


   $result=mysql_query($query);

   if (!$result) {
      die("Invalid query: " . mysql_error());
   };

   //mysqli_close();

/*
   while ($row = mysqli_fetch_assoc($result)) {
      echo $row['id'];
      echo $row['description'];
   }
*/

   // Free the resources associated with the result set
   // This is done automatically at the end of the script
   mysql_free_result($result);

   return $result;

}

?>
