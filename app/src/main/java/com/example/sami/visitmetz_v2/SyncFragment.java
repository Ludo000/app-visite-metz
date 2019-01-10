package com.example.sami.visitmetz_v2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sami.visitmetz_v2.models.SiteData;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    public Button buttonUpload;
    public Button buttonDownload;
    public Button buttonShow;
    public TextView textSyncOutput;
    public SyncButtonUploadListener syncButtonUploadListener;
    public SyncButtonDownloadListener syncButtonDownloadListener;
    public SyncButtonShowListener syncButtonShowListener;
    public ProgressBar spinner;
    public ListView listViewSync;
    public List<String> listSite= new ArrayList<String>();
    public ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        this.buttonDownload = view.findViewById(R.id.buttonDownload);
        this.buttonUpload = view.findViewById(R.id.buttonUpload);
        this.buttonShow = view.findViewById(R.id.buttonShow);
        this.textSyncOutput = view.findViewById(R.id.textSyncOutput);
        this.syncButtonUploadListener = new SyncButtonUploadListener(this);
        this.syncButtonDownloadListener = new SyncButtonDownloadListener(this);
        this.syncButtonShowListener = new SyncButtonShowListener(this);
        this.buttonUpload.setOnClickListener(this.syncButtonUploadListener);
        this.buttonDownload.setOnClickListener(this.syncButtonDownloadListener);
        this.buttonShow.setOnClickListener(this.syncButtonShowListener);
        this.spinner = view.findViewById(R.id.progressBar1);
        this.listViewSync = view.findViewById(R.id.listViewSync);
        this.adapter = new ArrayAdapter<String>(view.getContext(), R.layout.text_view_sync,listSite);
        this.listViewSync.setAdapter(adapter);
        this.spinner.setVisibility(View.VISIBLE);
        this.buttonDownload.setEnabled(false);
        this.buttonUpload.setEnabled(false);
        this.buttonShow.setEnabled(false);
        new GetTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");


        return view;
    }
}
