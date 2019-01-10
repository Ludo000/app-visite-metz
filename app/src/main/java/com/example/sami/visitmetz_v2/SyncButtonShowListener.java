package com.example.sami.visitmetz_v2;

import android.view.View;

public class SyncButtonShowListener extends SyncButtonListener {
    public SyncButtonShowListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.syncFragment.buttonDownload.setEnabled(false);
        this.syncFragment.buttonUpload.setEnabled(false);
        this.syncFragment.buttonShow.setEnabled(false);
        this.syncFragment.textSyncOutput.setText("");
        this.syncFragment.spinner.setVisibility(View.VISIBLE);

        new GetTask(this.syncFragment).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");


    }
    @Override
    public String getOutput(){
        return ("");
    }
}
