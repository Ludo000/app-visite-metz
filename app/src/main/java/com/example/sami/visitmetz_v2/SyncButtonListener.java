package com.example.sami.visitmetz_v2;

import android.view.View;

public class SyncButtonListener implements View.OnClickListener {
    public SyncFragment syncFragment;
    public  SyncButtonListener(SyncFragment syncFragment){
        this.syncFragment=syncFragment;
    }
    @Override
    public void onClick(View v) {

    }
    public String getOutput(){
        return null;
    }
}
