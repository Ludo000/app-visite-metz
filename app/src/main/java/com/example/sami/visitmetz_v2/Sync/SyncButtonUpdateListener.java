package com.example.sami.visitmetz_v2.Sync;

import android.view.View;

public class SyncButtonUpdateListener extends SyncButtonListener {
    public SyncButtonUpdateListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.prepareUI();
        this.syncFragment.listSite.clear();
        this.syncFragment.adapter.notifyDataSetChanged();
        new GetTask(this.syncFragment).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");
    }
    @Override
    public String getOutput(){
        return ("");
    }
}
