
<?php
    function coBdd()
    {
        $db_handle = new PDO('sqlite:VisiteMetz.db');
        $db_handle->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $db_handle->exec("CREATE TABLE IF NOT EXISTS sites_table (_ID INTEGER PRIMARY KEY AUTOINCREMENT, NOM TEXT, LATITUDE FLOAT, LONGITUDE FLOAT, ADRESSE_POSTALE TEXT, idCategorie INTEGER, CATEGORIE TEXT, RESUME TEXT, IMAGE TEXT);");
        return $db_handle;
    }
    function secure($s)
    {
        return trim(htmlspecialchars($s));
    }
    function error($s)
    {
        echo "ERR : ".$s;
    }
    function success($s)
    {
        echo "OK : ".$s;
    }

?>
