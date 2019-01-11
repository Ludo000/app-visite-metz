package com.example.sami.visitmetz_v2.Sync;

import android.content.ContentResolver;
import android.view.View;

public class SyncButtonListener implements View.OnClickListener {
    public SyncFragment syncFragment;
    public ContentResolver resolver;

    public  SyncButtonListener(SyncFragment syncFragment){
        this.syncFragment=syncFragment;

    }
    @Override
    public void onClick(View v) {



    }
    public String getOutput(){
        return null;
    }
    public void prepareUI(){
        this.syncFragment.buttonSync.setEnabled(false);
        this.syncFragment.buttonUpdate.setEnabled(false);
        this.syncFragment.textSyncOutput.setText("");
        this.syncFragment.spinner.setVisibility(View.VISIBLE);
        this.syncFragment.textSyncOutput.setText(this.getOutput());
    }
    public void giveBackUI(boolean resetList){
        this.syncFragment.buttonSync.setEnabled(true);
        this.syncFragment.buttonUpdate.setEnabled(true);

        this.syncFragment.spinner.setVisibility(View.GONE);

    }
}
