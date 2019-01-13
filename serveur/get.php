<?php
    include("tools.php");
    
    try 
    {
        $db_handle = coBdd();
        if(isset($_GET['ID']))
        {
            $id = secure($_GET['ID']);
            $req = $db_handle->prepare("SELECT _ID, NOM, LATITUDE, LONGITUDE, ADRESSE_POSTALE, CATEGORIE, RESUME, IMAGE FROM sites_table WHERE _ID=?");
            $req->execute(array($id));
        }
        else {
            $req = $db_handle->query("SELECT * FROM sites_table;");
        }
        $result = $req->fetchAll();

        $json = json_encode($result,JSON_FORCE_OBJECT);
        
        echo $json;        
    } 
    catch (Exception $e) 
    {
        die("ERR : ".$e->getMessage());
    }

        
?>
