package com.example.sami.visitmetz_v2.Sync;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class HttpTask extends AsyncTask<String, Integer, String> {
    private SyncFragment syncFragment;
    private Boolean andOneMoreRefresh;
    private Boolean andGiveBackUI;
    public HttpTask(SyncFragment syncFragment, Boolean andOneMoreRefresh, Boolean andGiveBackUI){
        this.syncFragment=syncFragment;
        this.andOneMoreRefresh=andOneMoreRefresh;
        this.andGiveBackUI=andGiveBackUI;

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
        this.syncFragment.loadSiteIntoLocalBdd(result);
        if(andOneMoreRefresh)
            new HttpTask(this.syncFragment, false, true).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");
        if(this.andGiveBackUI)
            this.syncFragment.giveBackUI();
    }
}