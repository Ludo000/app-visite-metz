package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.models.SiteData;

public class SyncButtonUploadListener extends SyncButtonListener {

    public SyncButtonUploadListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.syncFragment.buttonDownload.setEnabled(false);
        this.syncFragment.buttonUpload.setEnabled(false);
        String currentText = this.syncFragment.textSyncOutput.getText().toString();

        this.resolver = this.syncFragment.getActivity().getContentResolver();
        String[] projection = new String[]{"_id","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

        @SuppressLint("Recycle")
        Cursor foundSite = resolver.query(SitesProvider.CONTENT_URI, projection, null, null, null);

        if(foundSite!=null) {
            while (foundSite.moveToNext()) {

                String name = foundSite.getString(foundSite.getColumnIndex("NOM"));
                double latitude = (double) foundSite.getColumnIndex("LATITUDE");
                double longitude = (double) foundSite.getColumnIndex("LONGITUDE");
                String adresse = foundSite.getString(foundSite.getColumnIndex("ADRESSE_POSTALE"));
                String categorie = foundSite.getString(foundSite.getColumnIndex("CATEGORIE"));
                String resume = foundSite.getString(foundSite.getColumnIndex("RESUME"));
                byte[] image = foundSite.getBlob(foundSite.getColumnIndex("IMAGE"));

                new GetUrlContentTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/add.php"
                        + "?NOM=" + name
                        + "&LATITUDE=" + latitude
                        + "&LONGITUDE=" + longitude
                        + "&ADRESSE_POSTALE=" + adresse
                        + "&CATEGORIE=" + categorie
                        + "&RESUME=" + resume
                        + "&IMAGE=" + Base64.encodeToString(image, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
            }
        }
    }
    @Override
    public String getOutput(){

        return ("Téléversement de la base de données en cours...\n");
    }
}
