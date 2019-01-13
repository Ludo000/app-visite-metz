package com.example.sami.visitmetz_v2.Sync;

import android.view.View;

import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

public class SyncButtonSupprListener extends SyncButtonListener {
    private String id;
    private SiteData siteToRemove;
    public SyncButtonSupprListener(SyncFragment syncFragment, String id, SiteData siteToRemove){
        super(syncFragment);
        this.id=id;
        this.siteToRemove=siteToRemove;
    }
    @Override
    public void onClick(View v) {
        this.prepareUI();
        new HttpTask(this.syncFragment, true, true).execute("https://www.mettreauclair.fr/appVisiteMetz/delete.php?ID=" + this.id);
        this.syncFragment.listSiteData.remove(siteToRemove);
        this.syncFragment.cardListFragment.cardArrayAdapter = new SiteDataArrayAdapter(this.syncFragment.getContext(), R.layout.recycle_items, this.syncFragment);
        this.syncFragment.cardListFragment.listView.setAdapter(this.syncFragment.cardListFragment.cardArrayAdapter);
        for (SiteData site : this.syncFragment.listSiteData) {
            this.syncFragment.cardListFragment.cardArrayAdapter.add(site);
        }
    }
    @Override
    public String getOutput(){
        return ("Suppression en cours...");
    }
}
