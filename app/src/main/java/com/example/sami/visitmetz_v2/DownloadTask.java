package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;
import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.models.SiteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private  SyncButtonListener syncButtonListener;
    public DownloadTask(SyncButtonListener syncButtonListener){
        this.syncButtonListener=syncButtonListener;

    }
    protected String doInBackground(String... urls) {
        this.syncButtonListener.syncFragment.textSyncOutput.setText(this.syncButtonListener.getOutput() + "\n");

        URL url = null;
        try {
            url = new URL(urls[0]);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        String content = "", line;

        try {
            connection.connect();
            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = rd.readLine()) != null)
                content += line + "\n";

        }
        catch (IOException e) {
            e.printStackTrace();
        }



        return content;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        if(result.substring(0,1).equals("{")) {
            try {
                JSONObject res = new JSONObject(result);
                Iterator<String> keys = res.keys();
                String printOut="";

                while(keys.hasNext()) {
                    String key = keys.next();
                    if (res.get(key) instanceof JSONObject) {
                        String nom, adresse, categorie, resume;
                        int id, id_ext;
                        double latitude, longitude;
                        byte[] image;

                        id = 0;
                        id_ext = Integer.parseInt(((JSONObject) res.get(key)).getString("_ID"));
                        nom = ((JSONObject) res.get(key)).getString("NOM");
                        latitude = Double.parseDouble (((JSONObject) res.get(key)).getString("LATITUDE"));
                        longitude = Double.parseDouble (((JSONObject) res.get(key)).getString("LONGITUDE"));
                        adresse = ((JSONObject) res.get(key)).getString("ADRESSE_POSTALE");
                        categorie = ((JSONObject) res.get(key)).getString("CATEGORIE");
                        resume = ((JSONObject) res.get(key)).getString("RESUME");
                        image = Base64.decode(((JSONObject) res.get(key)).getString("IMAGE"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);

                        this.syncButtonListener.resolver = this.syncButtonListener.syncFragment.getActivity().getContentResolver();

                        //On cherche si duplica
                        String[] projection = new String[]{"_id","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};
                        @SuppressLint("Recycle")
                        Cursor foundSite = this.syncButtonListener.resolver.query(SitesProvider.CONTENT_URI, projection, "NOM = ? AND LATITUDE=? AND LONGITUDE=? ", new String[]{nom, Double.toString(latitude), Double.toString(longitude)}, null);

                        if(foundSite!=null){
                            if(foundSite.moveToFirst()){
                                Log.e("ERR : duplica", nom);
                            }
                            else{
                                //On ajoute la ligne
                                ContentValues values = new ContentValues();
                                values.put("ID_EXT", id_ext);
                                values.put("NOM", nom);
                                values.put("LATITUDE", latitude);
                                values.put("LONGITUDE", longitude);
                                values.put("ADRESSE_POSTALE", adresse);
                                values.put("CATEGORIE", categorie);
                                values.put("RESUME", resume);
                                values.put("IMAGE", image);
                                this.syncButtonListener.resolver.insert(SitesProvider.CONTENT_URI, values);

                                //Ajout de la catégorie
                                String[] projectionCategorie = new String[]{"_id", "nom"};

                                    //test de l'unicité de la catégorie
                                    @SuppressLint("Recycle")
                                    Cursor foundCategorie = this.syncButtonListener.resolver.query(CategoriesProvider.CONTENT_URI, projectionCategorie, "nom = ?", new String[]{categorie}, null);
                                    if(foundCategorie!=null){
                                        if(foundCategorie.moveToFirst()){
                                            Log.e("ERR : duplica categorie", nom);
                                        }
                                        else{
                                            ContentValues valuesCategorie = new ContentValues();
                                            valuesCategorie.put("nom", categorie);
                                            this.syncButtonListener.resolver.insert(CategoriesProvider.CONTENT_URI, valuesCategorie);

                                        }
                                    }




                            }

                        }
                    }
                }
                this.syncButtonListener.syncFragment.textSyncOutput.setText(this.syncButtonListener.getOutput() + "Traitement terminé avec succès ! \n");



            } catch (JSONException e) {
                Log.e("json", e.getMessage());
                e.printStackTrace();
            }

        }
        else {
            this.syncButtonListener.syncFragment.textSyncOutput.setText(this.syncButtonListener.getOutput() + result + "\n");

        }
        this.syncButtonListener.syncFragment.buttonDownload.setEnabled(true);
        this.syncButtonListener.syncFragment.buttonUpload.setEnabled(true);
        this.syncButtonListener.syncFragment.buttonShow.setEnabled(true);
        this.syncButtonListener.syncFragment.spinner.setVisibility(View.GONE);

    }
}