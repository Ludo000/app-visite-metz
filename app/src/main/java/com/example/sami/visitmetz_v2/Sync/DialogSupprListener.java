package com.example.sami.visitmetz_v2.Sync;

import android.content.DialogInterface;

public class DialogSupprListener implements DialogInterface.OnClickListener {
    private SyncFragment syncFragment;
    private String idSiteToDelete;
    public DialogSupprListener(SyncFragment syncFragment, String idSiteToDelete){
        this.syncFragment=syncFragment;
        this.idSiteToDelete=idSiteToDelete;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        this.syncFragment.syncButtonSyncListener.prepareUI();
        new HttpTask(this.syncFragment,true, false).execute("https://www.mettreauclair.fr/appVisiteMetz/delete.php?ID=" + this.idSiteToDelete);
    }
}
