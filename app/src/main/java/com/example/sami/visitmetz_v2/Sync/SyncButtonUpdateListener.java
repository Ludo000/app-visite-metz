package com.example.sami.visitmetz_v2.Sync;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class SyncButtonUpdateListener extends SyncButtonListener {
    public SyncButtonUpdateListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.prepareUI();
        this.syncFragment.listSiteData.clear();
        this.syncFragment.cardListFragment.cardArrayAdapter.notifyDataSetChanged();
        Toast.makeText(this.syncFragment.getContext(),this.getOutput(),Toast.LENGTH_SHORT).show();
        new HttpTask(this.syncFragment, false, true).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");
    }
    @Override
    public String getOutput(){
        return ("Rafraichissement en cours...");
    }
}
