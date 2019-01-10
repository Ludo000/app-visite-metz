package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Base64;
import android.view.View;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;

public class SyncButtonUploadListener extends SyncButtonListener {
    public SyncButtonUploadListener(SyncFragment syncFragment){
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

        this.resolver = this.syncFragment.getActivity().getContentResolver();
        String[] projection = new String[]{"_id","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

        @SuppressLint("Recycle")
        Cursor foundSite = resolver.query(SitesProvider.CONTENT_URI, projection, null, null, null);

        if(foundSite!=null) {
            while (foundSite.moveToNext()) {

                String name = foundSite.getString(foundSite.getColumnIndex("NOM"));
                double latitude = Double.parseDouble(foundSite.getString(3));
                double longitude = Double.parseDouble(foundSite.getString(4));
                String adresse = foundSite.getString(foundSite.getColumnIndex("ADRESSE_POSTALE"));
                String categorie = foundSite.getString(foundSite.getColumnIndex("CATEGORIE"));
                String resume = foundSite.getString(foundSite.getColumnIndex("RESUME"));
                byte[] image = foundSite.getBlob(foundSite.getColumnIndex("IMAGE"));

                new UploadTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/add.php"
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
