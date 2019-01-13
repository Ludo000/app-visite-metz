 <?php
    include("tools.php");

    if(isset($_GET['confirm']))
    {
        try 
        {
            $db_handle = coBdd();
            $db_handle->exec("DROP TABLE IF EXISTS sites_table");
            success("drop");

        } catch (Exception $e) {
           die("ERR : ".$e->getMessage());
        }
    }
    else 
        error("confirm ?");
?>
