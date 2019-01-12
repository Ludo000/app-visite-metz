package com.example.sami.visitmetz_v2.Sync;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sami.visitmetz_v2.R;

public class SiteDataListFragment extends Fragment {

    public SiteDataArrayAdapter cardArrayAdapter;
    public ListView listView;

    public SiteDataListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.site_data_listview, container, false);
        this.listView = view.findViewById(R.id.site_data_listView);

        return view;
    }


}