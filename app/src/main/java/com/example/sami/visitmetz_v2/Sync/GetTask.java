package com.example.sami.visitmetz_v2.Sync;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.sami.visitmetz_v2.models.SiteData;

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
    private SyncFragment syncFragment;
    public GetTask(SyncFragment syncFragment){
        this.syncFragment=syncFragment;
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
            this.syncFragment.listSiteData = new ArrayList<>();
            try {
                JSONObject res = new JSONObject(result);
                Iterator<String> keys = res.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (res.get(key) instanceof JSONObject) {
                        SiteData siteTemp = new SiteData(
                                0,
                                Integer.parseInt(((JSONObject) res.get(key)).getString("_ID")),
                                ((JSONObject) res.get(key)).getString("NOM"),
                                Double.parseDouble(((JSONObject) res.get(key)).getString("LATITUDE")),
                                Double.parseDouble(((JSONObject) res.get(key)).getString("LONGITUDE")),
                                ((JSONObject) res.get(key)).getString("ADRESSE_POSTALE"),
                                ((JSONObject) res.get(key)).getString("CATEGORIE"),
                                ((JSONObject) res.get(key)).getString("RESUME"),
                                Base64.decode(((JSONObject) res.get(key)).getString("IMAGE"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));

                        this.syncFragment.listSiteData.add(siteTemp);
                        this.syncFragment.listSite.add(siteTemp.getID() + " " + siteTemp.getIDEXT() + " " + siteTemp.getNom() + " " + siteTemp.getLongitude() + " " + siteTemp.getLatitude() + " " + siteTemp.getAdresse() + " " + siteTemp.getCategorie() + " " + siteTemp.getResume()  + "\n");
                    }
                }
                this.syncFragment.adapter.notifyDataSetChanged();
                this.syncFragment.spinner.setVisibility(View.GONE);



            } catch (JSONException e) {
                Log.e("json", e.getMessage());
                e.printStackTrace();
            }
        }

        this.syncFragment.syncButtonSyncListener.giveBackUI(false);
    }
}