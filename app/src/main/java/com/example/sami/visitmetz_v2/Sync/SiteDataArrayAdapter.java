package com.example.sami.visitmetz_v2.Sync;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

import java.util.ArrayList;
import java.util.List;

public class SiteDataArrayAdapter extends ArrayAdapter<SiteData> {
    private List<SiteData> siteDataList = new ArrayList<SiteData>();
    private SyncFragment syncFragment;
    static class SiteDataViewHolder {
        TextView titleTextView;
        ImageView coverImageView;
        ImageView shareImageView;
        ImageView editImageView;
        ImageView deleteImageView;
        ImageView likeImageView;
    }

    public SiteDataArrayAdapter(Context context, int textViewResourceId, SyncFragment syncFragment) {
        super(context, textViewResourceId);
        this.syncFragment=syncFragment;
    }

    @Override
    public void add(SiteData object) {
        siteDataList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.siteDataList.size();
    }

    @Override
    public SiteData getItem(int index) {
        return this.siteDataList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SiteDataViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.recycle_items, parent, false);
            viewHolder = new SiteDataViewHolder();
            viewHolder.titleTextView = row.findViewById(R.id.titleTextView);
            viewHolder.coverImageView= row.findViewById(R.id.coverImageView);
            viewHolder.editImageView= row.findViewById(R.id.editImageView);
            viewHolder.deleteImageView= row.findViewById(R.id.deleteImageView);
            viewHolder.likeImageView= row.findViewById(R.id.likeImageView);
            row.setTag(viewHolder);
        } else {
            viewHolder = (SiteDataViewHolder) row.getTag();
        }
        SiteData siteData = getItem(position);
        viewHolder.titleTextView.setText(siteData.getNom());
        Bitmap bitmap = BitmapFactory.decodeByteArray(siteData.getImage(), 0, siteData.getImage().length);
        viewHolder.coverImageView.setImageBitmap(bitmap);
        viewHolder.coverImageView.setTag(bitmap);
        viewHolder.likeImageView.setVisibility(View.INVISIBLE);
        viewHolder.editImageView.setVisibility(View.INVISIBLE);
        viewHolder.deleteImageView.setOnClickListener(new SyncButtonSupprListener(this.syncFragment, Integer.toString(siteData.getIDEXT()),siteData));

        return row;
    }
}