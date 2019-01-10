package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetTask extends AsyncTask<String, Integer, String> {
    private  SyncFragment syncFragment;
    private List<SiteData> listSiteData;
    public GetTask(SyncFragment syncFragment){
        this.syncFragment=syncFragment;
        this.listSiteData = new ArrayList<>();

    }
    protected String doInBackground(String... urls) {

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
            String siteToShow = "";
            try {
                JSONObject res = new JSONObject(result);
                Iterator<String> keys = res.keys();
                String printOut = "";

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (res.get(key) instanceof JSONObject) {
                        String nom, adresse, categorie, resume;
                        int id, id_ext;
                        double latitude, longitude;
                        byte[] image;

                        id = 0;
                        id_ext = Integer.parseInt(((JSONObject) res.get(key)).getString("_ID"));
                        nom = ((JSONObject) res.get(key)).getString("NOM");
                        latitude = Double.parseDouble(((JSONObject) res.get(key)).getString("LATITUDE"));
                        longitude = Double.parseDouble(((JSONObject) res.get(key)).getString("LONGITUDE"));
                        adresse = ((JSONObject) res.get(key)).getString("ADRESSE_POSTALE");
                        categorie = ((JSONObject) res.get(key)).getString("CATEGORIE");
                        resume = ((JSONObject) res.get(key)).getString("RESUME");
                        image = Base64.decode(((JSONObject) res.get(key)).getString("IMAGE"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);

                        siteToShow = "id :" + Integer.toString(id) + " id_ext :" + Integer.toString(id_ext) + " / nom : " + nom + "  / latitude : " + Double.toString(latitude) + " / longitude : " + Double.toString(longitude) + " / adresse : " + adresse + " / catégorie : " + categorie + " / resumé : " + resume + "\n";
                        this.syncFragment.listSite.add(siteToShow);
                    }
                }
                this.syncFragment.adapter.notifyDataSetChanged();
                //this.syncFragment.textSyncOutput.setText(siteToShow);
                this.syncFragment.spinner.setVisibility(View.GONE);


            } catch (JSONException e) {
                Log.e("json", e.getMessage());
                e.printStackTrace();
            }
        }

        this.syncFragment.buttonDownload.setEnabled(true);
        this.syncFragment.buttonUpload.setEnabled(true);
        this.syncFragment.buttonShow.setEnabled(true);
    }
}