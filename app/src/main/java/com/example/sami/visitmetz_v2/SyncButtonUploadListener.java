package com.example.sami.visitmetz_v2;

import android.view.View;

public class SyncButtonUploadListener extends SyncButtonListener {
    public SyncButtonUploadListener(SyncFragment syncFragment){
        super(syncFragment);

    }
    @Override
    public void onClick(View v) {
        this.syncFragment.buttonDownload.setEnabled(false);
        this.syncFragment.buttonUpload.setEnabled(false);
        String currentText = this.syncFragment.textSyncOutput.getText().toString();

        String nom ="nomTest";
        String latitude="latitudeTest";
        String longitude="longitudeTest";
        String adresse_postale="adressePostaleTest";
        String categorie="categorieTest";
        String resume="resumeTest";
        String image="imageTest";

        new GetUrlContentTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/add.php"
                + "?NOM=" + nom
                + "&LATITUDE=" + latitude
                + "&LONGITUDE=" + longitude
                + "&ADRESSE_POSTALE=" + adresse_postale
                + "&CATEGORIE=" + categorie
                + "&RESUME=" + resume
                + "&IMAGE=" + image);
    }
    @Override
    public String getOutput(){
        return ("Téléversement de la base de données en cours...\n");
    }
}
