package com.example.sami.visitmetz_v2;

import android.view.View;

public class SyncButtonDownloadListener extends SyncButtonListener {
    public SyncButtonDownloadListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.syncFragment.buttonDownload.setEnabled(false);
        this.syncFragment.buttonUpload.setEnabled(false);
        this.syncFragment.buttonShow.setEnabled(false);
        this.syncFragment.textSyncOutput.setText("");
        this.syncFragment.spinner.setVisibility(View.VISIBLE);
        String currentText = this.syncFragment.textSyncOutput.getText().toString();

        new DownloadTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");


    }
    @Override
    public String getOutput(){
        return ("Téléchargement de la base de données en cours...\n");
    }
}
