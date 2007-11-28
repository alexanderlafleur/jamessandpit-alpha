
<?php

include("connect_to_database.php");

$query = "select * from disk order by label";

$result=mysql_query($query);

// $num=mysql_numrows($result);

mysql_close();

echo "<b><center>Database Output</center></b><br><br>";

$i=0;
while ($i < $num) {

        $id=mysql_result($result,$i,"id");
        $label=mysql_result($result,$i,"label");
        $medianumber=mysql_result($result,$i,"medianumber");

        echo "<b>$label</b> id  - $medianumber<br>";

        $i++;
}


?>
