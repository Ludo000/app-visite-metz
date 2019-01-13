<?php
    include("tools.php");
    echo '<html>
            <head></head>
            <body>
            ';
    try 
    {
        $db_handle = coBdd();
        $req = $db_handle->query("SELECT * FROM sites_table;");
        $result = $req->fetchAll();
        echo '<table border="1">
                <tr bgcolor="#538cc6">
                    <th>_ID</th>
                    <th>NOM</th>
                    <th>LATITUDE</th>
                    <th>LONGITUDE</th>
                    <th>ADRESSE_POSTALE</th>
                    <th>idCategorie</th>
                    <th>CATEGORIE</th>
                    <th>RESUME</th>
                    <th>IMAGE</th>
                </tr>
                ';
        $col=true;
        foreach($result as $i) {
            $col=!$col;
            if($col)
                echo '<tr bgcolor=" #b3cce6">
                ';
            else {
                echo '<tr bgcolor="#F1F1F1">
                ';
            }
                echo '  <td>'.$i['_ID'].'</td>
                ';
                echo '  <td>'.$i['NOM'].'</td>
                ';
                echo '  <td>'.$i['LATITUDE'].'</td>
                ';
                echo '  <td>'.$i['LONGITUDE'].'</td>
                ';
                echo '  <td>'.$i['ADRESSE_POSTALE'].'</td>
                ';
                echo '  <td>'.$i['idCategorie'].'</td>
                ';
                echo '  <td>'.$i['CATEGORIE'].'</td>
                ';
                echo '  <td>'.$i['RESUME'].'</td>
                ';
                 echo '  <td> '.substr($i['IMAGE'],0,100).'... </td>
                ';

            echo '</tr>
            ';
        }
        echo '</table>
        ';
        echo '<a href="drop.php?confirm">/!\ DROP /!\</a>';

    } 
    catch (Exception $e) 
    {
        die("ERR : ".$e->getMessage());
    }
echo '</body>
</html>';

        
?>
