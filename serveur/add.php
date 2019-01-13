<?php
    include("tools.php");
    
    //?NOM=truc&LATITUDE=3&LONGITUDE=2&ADRESSE_POSTALE=adresse_test&CATEGORIE=cat_test&RESUME=res_test&IMAGE=image_test
    if(isset($_GET['NOM'], $_GET['LATITUDE'], $_GET['LONGITUDE'], $_GET['ADRESSE_POSTALE'], $_GET['idCategorie'], $_GET['CATEGORIE'], $_GET['RESUME'], $_GET['IMAGE']))
    {
        $nom = secure($_GET['NOM']);
        $latitude = secure($_GET['LATITUDE']);
        $longitude = secure($_GET['LONGITUDE']);
        $adresse_postale = secure($_GET['ADRESSE_POSTALE']);
        $_idCategorie = secure($_GET['idCategorie']);
        $categorie = secure($_GET['CATEGORIE']);
        $resume = secure($_GET['RESUME']);
        $image = $_GET['IMAGE'];


        try 
        {
            $db_handle = coBdd();
            $req0 = $db_handle->prepare("SELECT _ID FROM sites_table WHERE NOM=? AND LATITUDE=? AND LONGITUDE=?");
            $req0->execute(array($nom, $latitude, $longitude));
            $result0 = $req0->fetch();

            if(isset($result0['_ID'])){
                $req = $db_handle->prepare("DELETE FROM sites_table WHERE _ID=?");
                $req->execute(array($result0['_ID']));
                success("delete ".$result0['_ID']);
            }

            $req = $db_handle->prepare("INSERT INTO sites_table (_ID, NOM, LATITUDE, LONGITUDE, ADRESSE_POSTALE, idCategorie, CATEGORIE, RESUME, IMAGE) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");
            $req->execute(array($nom, $latitude, $longitude, $adresse_postale, $_idCategorie, $categorie, $resume, $image));
            success("Les données ont été ajoutées avec succès");
          
        } 
        catch (Exception $e) 
        {
            die("ERR : ".$e->getMessage());
        }
    }
    else 
        error("param ?");

        
?>
