package com.example.sami.visitmetz_v2.Sync;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;
import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.CustomMarker;
import com.example.sami.visitmetz_v2.models.SiteData;

public class SyncButtonSyncListener extends SyncButtonListener {
    public SyncButtonSyncListener(SyncFragment syncFragment){
        super(syncFragment);
    }
    @Override
    public void onClick(View v) {
        this.prepareUI();

        //Chargement
        for (SiteData siteData : this.syncFragment.listSiteData) {
            this.resolver = this.syncFragment.getActivity().getContentResolver();
            //On cherche si duplica
            String[] projection = new String[]{"_id", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "_idCategorie", "RESUME", "IMAGE"};
            @SuppressLint("Recycle")
            Cursor foundSite = this.resolver.query(SitesProvider.CONTENT_URI, projection, "NOM = ? AND LATITUDE=? AND LONGITUDE=? ", new String[]{siteData.getNom(), Double.toString(siteData.getLatitude()), Double.toString(siteData.getLongitude())}, null);

            if (foundSite != null) {
                if (foundSite.moveToFirst()) {
                    Log.e("ERR : duplica", siteData.getNom());
                } else {
                    //On ajoute la ligne
                    ContentValues values = new ContentValues();
                    values.put("ID_EXT", siteData.getIDEXT());
                    values.put("NOM", siteData.getNom());
                    values.put("LATITUDE", siteData.getLatitude());
                    values.put("LONGITUDE", siteData.getLongitude());
                    values.put("ADRESSE_POSTALE", siteData.getAdresse());
                    values.put("RESUME", siteData.getResume());
                    values.put("IMAGE", siteData.getImage());

                    //Ajout de la catégorie
                    String[] projectionCategorie = new String[]{"_idCategorie", "nom"};
                    String[] selectionargCategorie = new String[]{"" + siteData.getCategorie()};

                    //test de l'unicité de la catégorie
                    @SuppressLint("Recycle")
                    Cursor foundCategorie = this.resolver.query(CategoriesProvider.CONTENT_URI, projectionCategorie, "nom = ?", selectionargCategorie, null);
                    if (foundCategorie != null) {
                        if (foundCategorie.moveToFirst()) {
                            Log.e("ERR : duplica categorie", siteData.getCategorie());
                        } else {
                            // si pas de duplica
                            ContentValues valuesCategorie = new ContentValues();
                            valuesCategorie.put("nom", siteData.getCategorie());
                            this.resolver.insert(CategoriesProvider.CONTENT_URI, valuesCategorie);

                            //on récupère l'id de la catégorie ainsi créée
                            @SuppressLint("Recycle")
                            Cursor foundCategorieId = this.resolver.query(CategoriesProvider.CONTENT_URI, new String[]{"_idCategorie", "nom"}, "nom = ?", new String[]{"" + siteData.getCategorie()}, null);
                            if (foundCategorieId != null) {
                                if (foundCategorieId.moveToFirst()) {
                                    siteData.setCategorie(Integer.parseInt(foundCategorieId.getString(foundCategorieId.getColumnIndex("_idCategorie"))));
                                }
                            }
                        }
                        values.put("_idCategorie", siteData.getIdCategorie());
                        this.resolver.insert(SitesProvider.CONTENT_URI, values);


                    }
                }
            }
        }

        //Upload
        this.resolver = this.syncFragment.getActivity().getContentResolver();
        String[] projection = new String[]{"_id", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "_idCategorie", "RESUME", "IMAGE"};
        @SuppressLint("Recycle")
        Cursor foundSite = resolver.query(SitesProvider.CONTENT_URI, projection, null, null, null);

        if (foundSite != null) {
            while (foundSite.moveToNext()) {
                String[] projectionCategorie = new String[]{"_idCategorie", "nom"};
                String[] selectionargCategorie = new String[]{"" + foundSite.getString(foundSite.getColumnIndex("_idCategorie"))};

                @SuppressLint("Recycle")
                Cursor categorie = this.syncFragment.getActivity().getContentResolver().query(CategoriesProvider.CONTENT_URI, projectionCategorie, "_idCategorie = ?", selectionargCategorie, null);
                if (categorie != null) {
                    if (categorie.moveToFirst()) {
                        String nomCategorie = categorie.getString(categorie.getColumnIndex("nom"));

                        new HttpTask(this.syncFragment, foundSite.isLast(), false).execute("https://www.mettreauclair.fr/appVisiteMetz/add.php"
                                + "?NOM=" + foundSite.getString(foundSite.getColumnIndex("NOM"))
                                + "&LATITUDE=" + Double.parseDouble(foundSite.getString(3))
                                + "&LONGITUDE=" + Double.parseDouble(foundSite.getString(4))
                                + "&ADRESSE_POSTALE=" + foundSite.getString(foundSite.getColumnIndex("ADRESSE_POSTALE"))
                                + "&idCategorie=" + foundSite.getString(foundSite.getColumnIndex("_idCategorie"))
                                + "&CATEGORIE=" + nomCategorie
                                + "&RESUME=" + foundSite.getString(foundSite.getColumnIndex("RESUME"))
                                + "&IMAGE=" + Base64.encodeToString(foundSite.getBlob(foundSite.getColumnIndex("IMAGE")), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));


                    } else
                        Log.d("#### ==> ", "Categorie introuvable!");
                }
            }
        }
    }


    @Override
    public String getOutput(){
        return ("Synchronisation de la base de données en cours...");
    }
}
