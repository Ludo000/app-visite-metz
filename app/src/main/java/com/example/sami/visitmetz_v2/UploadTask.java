package com.example.sami.visitmetz_v2;

import android.os.AsyncTask;
import android.view.View;

import com.example.sami.visitmetz_v2.models.SiteData;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends AsyncTask<String, Integer, String> {
    private  SyncButtonListener syncButtonListener;
    private List<SiteData> listSiteData;
    public UploadTask(SyncButtonListener syncButtonListener){
        this.syncButtonListener=syncButtonListener;
        this.listSiteData = new ArrayList<>();

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
        this.syncButtonListener.syncFragment.textSyncOutput.setText(this.syncButtonListener.getOutput() + result + "\n");
        this.syncButtonListener.syncFragment.buttonDownload.setEnabled(true);
        this.syncButtonListener.syncFragment.buttonUpload.setEnabled(true);
        this.syncButtonListener.syncFragment.buttonShow.setEnabled(true);
        this.syncButtonListener.syncFragment.spinner.setVisibility(View.GONE);

    }
}