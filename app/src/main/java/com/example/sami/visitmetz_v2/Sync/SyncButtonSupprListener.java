package com.example.sami.visitmetz_v2.Sync;

import android.view.View;

public class SyncButtonSupprListener extends SyncButtonListener {
    public SyncButtonSupprListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.prepareUI();
        new GetTask(this.syncFragment).execute("https://www.mettreauclair.fr/appVisiteMetz/delete.php");
    }
    @Override
    public String getOutput(){
        return ("Suppression en cours...");
    }
}
