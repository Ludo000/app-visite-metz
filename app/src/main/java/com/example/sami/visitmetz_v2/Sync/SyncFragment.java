package com.example.sami.visitmetz_v2.Sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    public Button buttonSync;
    public FloatingActionButton buttonUpdate;
    public TextView textSyncOutput;
    public SyncButtonSyncListener syncButtonSyncListener;
    public SyncButtonUpdateListener syncButtonShowListener;
    public ProgressBar spinner;
    public ListView listViewSync;
    public List<String> listSite= new ArrayList<String>();
    public ArrayAdapter<String> adapter;
    public List<SiteData> listSiteData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        this.buttonSync = view.findViewById(R.id.buttonSync);
        this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        this.textSyncOutput = view.findViewById(R.id.textSyncOutput);
        this.syncButtonSyncListener = new SyncButtonSyncListener(this);
        this.syncButtonShowListener = new SyncButtonUpdateListener(this);
        this.buttonSync.setOnClickListener(this.syncButtonSyncListener);
        this.buttonUpdate.setOnClickListener(this.syncButtonShowListener);
        this.spinner = view.findViewById(R.id.progressBar1);
        this.listViewSync = view.findViewById(R.id.listViewSync);
        this.adapter = new ArrayAdapter<>(view.getContext(), R.layout.text_view_sync,listSite);
        this.listViewSync.setAdapter(adapter);
        this.spinner.setVisibility(View.VISIBLE);
        this.buttonSync.setEnabled(false);
        this.buttonUpdate.setEnabled(false);
        new GetTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");
        return view;
    }
}
