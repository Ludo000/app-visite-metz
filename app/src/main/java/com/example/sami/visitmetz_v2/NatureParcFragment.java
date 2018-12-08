package com.example.sami.visitmetz_v2;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class NatureParcFragment extends Fragment {

    ArrayList<SiteData> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());

        // get image from drawable
        Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.cathedrale_st_etienne);
        // convert bitmap to byte
        ByteArrayOutputStream bos1=new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
        byte[] img1=bos1.toByteArray();

        Bitmap bitmap2=BitmapFactory.decodeResource(getResources(), R.drawable.centre_pompidou);
        ByteArrayOutputStream bos2=new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
        byte[] img2=bos2.toByteArray();

        Bitmap bitmap3=BitmapFactory.decodeResource(getResources(), R.drawable.stade_st_symphorien);
        ByteArrayOutputStream bos3=new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, bos3);
        byte[] img3=bos3.toByteArray();

        databaseHelper.addData("Cathédrale Saint-Étienne", 49.120484, 6.176334,"Place d'Armes, 57000 Metz, France", "Sites historiques, monuments, musées et statues", "La cathédrale Saint-Étienne de Metz est la cathédrale catholique du diocèse de Metz, dans le département français de la Moselle en région Grand Est.",  img1);
        databaseHelper.addData("Centre Pompidou-Metz", 49.108465, 6.181730, "1 Parvis des Droits de l'Homme, 57020 Metz, France","Sites historiques, monuments, musées et statues", "Le centre Pompidou-Metz est un établissement public de coopération culturelle d’art situé à Metz, entre le parc de la Seille et la gare. Sa construction est réalisée dans le cadre de l’opération d’aménagement du quartier de l’Amphithéâtre.", img2);
        databaseHelper.addData("Stade St Symphorien", 49.109968, 6.159747, "3 Boulevard Saint-Symphorien, 57050 Longeville-lès-Metz, France", "Jeux et divertissements", "Le stade Saint-Symphorien est l'enceinte sportive principale de l'agglomération messine. C'est un stade consacré au football qui est utilisé par le Football Club de Metz.", img3);
        initializeList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<SiteData> list;

        MyAdapter(ArrayList<SiteData> Data) {
            list = Data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

            holder.titleTextView.setText(list.get(position).getNom());
            holder.coverImageView.setTag(list.get(position).getImage());
            holder.likeImageView.setTag(R.drawable.ic_thumb_up_black_24dp);
            holder.editImageView.setTag(R.drawable.ic_add_black);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView coverImageView;
        ImageView likeImageView;
        ImageView shareImageView;
        ImageView editImageView;

        MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            coverImageView = v.findViewById(R.id.coverImageView);
            likeImageView = v.findViewById(R.id.likeImageView);
            shareImageView = v.findViewById(R.id.shareImageView);
            editImageView = v.findViewById(R.id.editImageView);

            /*editImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatabaseHelper mDatabaseHelper1 = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        mDatabaseHelper1 = new DatabaseHelper(getContext());
                    }
                    int id = (int)editImageView.getTag();
                    if( id == R.drawable.ic_add_black)
                    {
                        AjouterSiteFragment ajouterSiteFragment = new AjouterSiteFragment();
                        String newSoilTemp = null, newAirTemp = null, newSoilMoist = null, newAirMoist = null;

                        String newName = titleTextView.getText().toString();
                        byte[] imageOut = ajouterSiteFragment.imageViewToByte(coverImageView);

                        for (String[] aPlantsSelection : plantsSelection) {

                            if (aPlantsSelection[0].equals(newName)) {
                                newSoilTemp = aPlantsSelection[1];
                                newAirTemp = aPlantsSelection[2];
                                newSoilMoist = aPlantsSelection[3];
                                newAirMoist = aPlantsSelection[4];
                            }
                        }

                        //Checks if it is not empty
                        if (titleTextView.getText().length() != 0) {

                            assert mDatabaseHelper1 != null;
                            mDatabaseHelper1.addData(newName, imageOut, newSoilTemp, newAirTemp, newSoilMoist, newAirMoist);

                            editImageView.setTag(R.drawable.ic_add_colored); //ic_liked
                            editImageView.setImageResource(R.drawable.ic_add_colored); //ic_liked

                            Toast.makeText(getActivity(),titleTextView.getText()+" wurde erfolgreich zu 'Meine Pflanzen' hinzugefügt", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(),"Formular ist leer",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        assert mDatabaseHelper1 != null;
                        mDatabaseHelper1.DeleteRecord(titleTextView.getText().toString());
                        editImageView.setTag(R.drawable.ic_add_black);
                        editImageView.setImageResource(R.drawable.ic_add_black);
                        Toast.makeText(getActivity(),titleTextView.getText()+" wurde von 'Meine Pflanzen' entfernt", Toast.LENGTH_SHORT).show();
                    }

                }
            });*/

            /*likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                int id = (int)likeImageView.getTag();
                if( id == R.drawable.ic_thumb_up_black_24dp){

                    likeImageView.setTag(R.drawable.ic_like_colored); //ic_liked
                    likeImageView.setImageResource(R.drawable.ic_like_colored); //ic_liked

                    Toast.makeText(getActivity(),titleTextView.getText()+" wurde erfolgreich zu 'Favoriten' hinzugefügt",Toast.LENGTH_SHORT).show();

                }else{

                    likeImageView.setTag(R.drawable.ic_thumb_up_black_24dp);
                    likeImageView.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    Toast.makeText(getActivity(),titleTextView.getText()+" wurde von 'Favoriten' entfernt",Toast.LENGTH_SHORT).show();
                }
                }
            });*/

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(coverImageView.getId())
                        + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                }
            });
        }
    }

    public void initializeList() {
        listitems.clear();

        databaseHelper = new DatabaseHelper(getActivity());
        Cursor data = databaseHelper.getData();
        while(data.moveToNext())
        {
            String nom = data.getString(1);
            Double latitude = Double.valueOf(data.getString(2));
            Double longitude = Double.valueOf(data.getString(3));
            String adresse = data.getString(4);
            String categorie = data.getString(5);
            String resume = data.getString(6);
            byte image[] = data.getBlob(7);

            listitems.add(new SiteData(nom, latitude, longitude, adresse, categorie, resume, image));
        }
    }
}
