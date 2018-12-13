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
        String currentText = this.syncFragment.textSyncOutput.getText().toString();

        new GetUrlContentTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");


    }
    @Override
    public String getOutput(){
        return ("Téléchargement de la base de données en cours...\n");
    }
}
