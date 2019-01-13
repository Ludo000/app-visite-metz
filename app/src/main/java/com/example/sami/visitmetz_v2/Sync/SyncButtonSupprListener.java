package com.example.sami.visitmetz_v2.Sync;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

public class SyncButtonSupprListener extends SyncButtonListener {
    private String idSiteToDelete;
    private AlertDialog.Builder builder;
    public SyncButtonSupprListener(SyncFragment syncFragment, String id, SiteData siteToRemove){
        super(syncFragment);
        this.idSiteToDelete=id;
        this.builder = new AlertDialog.Builder(this.syncFragment.getContext());

    }
    @Override
    public void onClick(View v) {
        builder = new AlertDialog.Builder(this.syncFragment.getContext());
        builder.setCancelable(false);
        builder.setTitle("Supprimer ce site ?");
        builder.setMessage("Êtes-vous sûr ?");
        builder.setPositiveButton("Oui", new DialogSupprListener(this.syncFragment,this.idSiteToDelete));
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }
    @Override
    public String getOutput(){
        return ("Suppression en cours...");
    }
}
