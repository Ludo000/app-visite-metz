package com.example.sami.visitmetz_v2.Sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.Sites.SitesOverviewFragment;
import com.example.sami.visitmetz_v2.models.SiteData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SyncFragment extends Fragment {
    public Button buttonSync;
    public FloatingActionButton buttonUpdate;
    public SyncButtonSyncListener syncButtonSyncListener;
    public SyncButtonUpdateListener syncButtonUpdateListener;
    public ProgressBar spinner;
    public List<SiteData> listSiteData;
    public SiteDataListFragment cardListFragment;
    public SitesOverviewFragment sitesOverviewFragment;
    public FrameLayout frameLayout;
    public ImageView hideLocale;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sitesOverviewFragment = new SitesOverviewFragment();
        this.listSiteData = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        this.buttonSync = view.findViewById(R.id.buttonSync);
        this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        this.spinner = view.findViewById(R.id.progressBar1);
        this.frameLayout = view.findViewById(R.id.fragment_container);
        this.hideLocale = view.findViewById(R.id.hideLocale);

        this.syncButtonSyncListener = new SyncButtonSyncListener(this);
        this.syncButtonUpdateListener = new SyncButtonUpdateListener(this);
        this.buttonSync.setOnClickListener(this.syncButtonSyncListener);
        this.buttonUpdate.setOnClickListener(this.syncButtonUpdateListener);
        this.hideLocale.setVisibility(View.VISIBLE);
        this.spinner.setVisibility(View.VISIBLE);
        this.buttonSync.setEnabled(false);
        this.buttonUpdate.setEnabled(false);

        this.cardListFragment = new SiteDataListFragment();
        this.cardListFragment.cardArrayAdapter = new SiteDataArrayAdapter(this.getContext(), R.layout.recycle_items, this);
        getFragmentManager().beginTransaction().replace(R.id.syncCardFragmentContainerLeft, this.cardListFragment);
        getFragmentManager().beginTransaction().replace(R.id.syncCardFragmentContainerRight, this.sitesOverviewFragment);

        this.cardListFragment.listView = view.findViewById(R.id.site_data_listView);
        this.cardListFragment.listView.setAdapter(this.cardListFragment.cardArrayAdapter);

        Toast.makeText(this.getContext(),"Récupération de la sauvegarde en cours...",Toast.LENGTH_SHORT).show();
        new HttpTask(this,false, true).execute("https://www.mettreauclair.fr/appVisiteMetz/get.php");

        return view;
    }

    public void giveBackUI(){
        this.buttonSync.setEnabled(true);
        this.buttonUpdate.setEnabled(true);
        this.cardListFragment.listView.setVisibility(View.VISIBLE);
        this.spinner.setVisibility(View.GONE);
        this.hideLocale.setVisibility(View.GONE);

    }

    public void loadSiteData(String result){


            this.cardListFragment.cardArrayAdapter = new SiteDataArrayAdapter(this.getContext(), R.layout.recycle_items, this);
            this.cardListFragment.listView.setAdapter(this.cardListFragment.cardArrayAdapter);
            this.listSiteData = new ArrayList<>();
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
                                Integer.parseInt(((JSONObject) res.get(key)).getString("idCategorie")),
                                ((JSONObject) res.get(key)).getString("CATEGORIE"),
                                ((JSONObject) res.get(key)).getString("RESUME"),
                                Base64.decode(((JSONObject) res.get(key)).getString("IMAGE"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));

                        this.listSiteData.add(siteTemp);
                        this.cardListFragment.cardArrayAdapter.add(siteTemp);
                    }
                }


            } catch (JSONException e) {
                Log.e("json", e.getMessage());
                e.printStackTrace();
            }

    }

}
