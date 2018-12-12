package com.example.sami.visitmetz_v2;

import android.view.View;

public class SyncButtonUploadListener implements View.OnClickListener {
    public SyncFragment syncFragment;

    public SyncButtonUploadListener(SyncFragment syncFragment){
        this.syncFragment=syncFragment;

    }
    @Override
    public void onClick(View v) {
        this.syncFragment.buttonDownload.setEnabled(false);
        this.syncFragment.buttonUpload.setEnabled(false);
        String currentText = this.syncFragment.textSyncOutput.getText().toString();
        this.syncFragment.textSyncOutput.setText(currentText + "Téléversement de la base de données en cours...\n");

        //Traitement


        this.syncFragment.buttonDownload.setEnabled(true);
        this.syncFragment.buttonUpload.setEnabled(true);
    }
}
