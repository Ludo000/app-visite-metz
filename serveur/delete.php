<?php
    include("tools.php");
    
    if(isset($_GET['ID']))
    {
        $id = secure($_GET['ID']);

        try 
        {
            $db_handle = coBdd();
            $req = $db_handle->prepare("DELETE FROM sites_table WHERE _ID=?");
            $req->execute(array($id));
            success("delete ".$id);
        
        } 
        catch (Exception $e) 
        {
            die("ERR : ".$e->getMessage());
        }
    }

        
?>
