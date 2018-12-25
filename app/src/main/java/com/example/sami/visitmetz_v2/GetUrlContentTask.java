package com.example.sami.visitmetz_v2;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

public class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    private  SyncButtonListener syncButtonListener;
    private List<SiteData> listSiteData;
    public GetUrlContentTask(SyncButtonListener syncButtonListener){
        this.syncButtonListener=syncButtonListener;
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

                        id = Integer.parseInt(((JSONObject) res.get(key)).getString("ID"));
                        id_ext = Integer.parseInt(((JSONObject) res.get(key)).getString("ID_EXT"));
                        nom = ((JSONObject) res.get(key)).getString("NOM");
                        latitude = Double.parseDouble (((JSONObject) res.get(key)).getString("LATITUDE"));
                        longitude = Double.parseDouble (((JSONObject) res.get(key)).getString("LONGITUDE"));
                        adresse = ((JSONObject) res.get(key)).getString("ADRESSE_POSTALE");
                        categorie = ((JSONObject) res.get(key)).getString("CATEGORIE");
                        resume = ((JSONObject) res.get(key)).getString("RESUME");
                        image = ((JSONObject) res.get(key)).getString("IMAGE").getBytes();

                        SiteData sd = new SiteData(id, id_ext, nom, latitude, longitude, adresse, categorie, resume, image);

                        this.listSiteData.add(sd);
                        printOut += sd.toString() + "\n";

                    }
                }

                this.syncButtonListener.syncFragment.textSyncOutput.setText(this.syncButtonListener.getOutput() + printOut + "\n");



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
    }
}