package com.example.sami.visitmetz_v2.Sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    public Button buttonSync;
    public FloatingActionButton buttonUpdate;
    public SyncButtonSyncListener syncButtonSyncListener;
    public SyncButtonUpdateListener syncButtonShowListener;
    public ProgressBar spinner;
    public List<SiteData> listSiteData;
    public SiteDataListFragment cardListFragment;

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
        this.syncButtonSyncListener = new SyncButtonSyncListener(this);
        this.syncButtonShowListener = new SyncButtonUpdateListener(this);
        this.buttonSync.setOnClickListener(this.syncButtonSyncListener);
        this.buttonUpdate.setOnClickListener(this.syncButtonShowListener);
        this.spinner = view.findViewById(R.id.progressBar1);
        this.spinner.setVisibility(View.VISIBLE);
        this.buttonSync.setEnabled(false);
        this.buttonUpdate.setEnabled(false);
        this.cardListFragment = new SiteDataListFragment();
        this.cardListFragment.cardArrayAdapter = new SiteDataArrayAdapter(this.getContext(), R.layout.recycle_items);
        getFragmentManager().beginTransaction().replace(R.id.syncCardFragmentContainer, this.cardListFragment);
        this.cardListFragment.listView = view.findViewById(R.id.site_data_listView);
        this.cardListFragment.listView.setAdapter(this.cardListFragment.cardArrayAdapter);
        Toast.makeText(this.getContext(),"Récupération de la sauvegarde en cours...",Toast.LENGTH_SHORT).show();
        new GetTask(this).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");
        return view;
    }
}
