package com.example.sami.visitmetz_v2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SyncFragment extends Fragment {
    public Button buttonUpload;
    public Button buttonDownload;
    public TextView textSyncOutput;
    public SyncButtonUploadListener syncButtonUploadListener;
    public SyncButtonDownloadListener syncButtonDownloadListener;

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
        this.textSyncOutput = view.findViewById(R.id.textSyncOutput);
        this.syncButtonUploadListener = new SyncButtonUploadListener(this);
        this.syncButtonDownloadListener = new SyncButtonDownloadListener(this);
        this.buttonUpload.setOnClickListener(this.syncButtonUploadListener);
        this.buttonDownload.setOnClickListener(this.syncButtonDownloadListener);

        return view;
    }
}
