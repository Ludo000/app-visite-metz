package com.example.sami.visitmetz_v2.Sync;

import android.content.ContentResolver;
import android.view.View;
import android.widget.Toast;

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
        this.syncFragment.cardListFragment.listView.setVisibility(View.INVISIBLE);
        this.syncFragment.spinner.setVisibility(View.VISIBLE);
        Toast.makeText(this.syncFragment.getContext(),this.getOutput(),Toast.LENGTH_SHORT).show();
    }
}
