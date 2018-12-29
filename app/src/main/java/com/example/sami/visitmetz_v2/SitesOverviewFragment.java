package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.models.SiteData;

import java.io.ByteArrayOutputStream;

public class SitesOverviewFragment extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView MyRecyclerView;
    MyAdapter adapter;
    TextView textViewNoData;

    ContentResolver resolver;

    EcouteurLoadEvenement ecouteurLoadEvenement;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*byte[] img1=getByteFromDrawable(Objects.requireNonNull(getDrawable(Objects.requireNonNull(getContext()), R.drawable.cathedrale_st_etienne)));

        byte[] img2=getByteFromDrawable(Objects.requireNonNull(getDrawable(getContext(), R.drawable.centre_pompidou)));

        byte[] img3=getByteFromDrawable(Objects.requireNonNull(getDrawable(getContext(), R.drawable.stade_st_symphorien)));

        // Add a new site record
        ContentValues sitesValues = contentValues("Cathédrale Saint-Étienne", 49.120484, 6.176334,"Place d'Armes, 57000 Metz, France", "Sites historiques, monuments, musées et statues", "La cathédrale Saint-Étienne de Metz est la cathédrale catholique du diocèse de Metz, dans le département français de la Moselle en région Grand Est.",  img1);

        Uri uri = getContext().getContentResolver().insert(
                SitesProvider.CONTENT_URI, sitesValues);

        // Add a new student record
        sitesValues = contentValues("Centre Pompidou-Metz", 49.108465, 6.181730, "1 Parvis des Droits de l'Homme, 57020 Metz, France","Sites historiques, monuments, musées et statues", "Le centre Pompidou-Metz est un établissement public de coopération culturelle d’art situé à Metz, entre le parc de la Seille et la gare. Sa construction est réalisée dans le cadre de l’opération d’aménagement du quartier de l’Amphithéâtre.", img2);

        uri = getContext().getContentResolver().insert(
                SitesProvider.CONTENT_URI, sitesValues);

        // Add a new student record
        sitesValues = contentValues("Stade St Symphorien", 49.109968, 6.159747, "3 Boulevard Saint-Symphorien, 57050 Longeville-lès-Metz, France", "Jeux et divertissements", "Le stade Saint-Symphorien est l'enceinte sportive principale de l'agglomération messine. C'est un stade consacré au football qui est utilisé par le Football Club de Metz.", img3);

        uri = getContext().getContentResolver().insert(
                SitesProvider.CONTENT_URI, sitesValues);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        textViewNoData = view.findViewById(R.id.textViewNoData);
        MyRecyclerView = view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        adapter= new MyAdapter(getContext(),null);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (MyRecyclerView != null) {
            MyRecyclerView.setAdapter(adapter);
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resolver = getContext().getContentResolver();

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        ecouteurLoadEvenement = new EcouteurLoadEvenement(getContext(), adapter, null);

        getLoaderManager().initLoader(0, null, ecouteurLoadEvenement);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        Drawable newIcon = item.getIcon();
        newIcon.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        item.setIcon(newIcon);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        ecouteurLoadEvenement = new EcouteurLoadEvenement(getContext(), adapter, s.trim().length() > 0 ? s : null);

        getLoaderManager().restartLoader(0, null, ecouteurLoadEvenement);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        onQueryTextSubmit(s);
        return true;
    }

    /*@NonNull
    public byte[] getByteFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }*/

    public class MyAdapter extends MyCursorAdapter {

        MyAdapter(Context context, Cursor cursor) {
            super(context, cursor);
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
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
            MyViewHolder holder = (MyViewHolder) viewHolder;

           // if (viewHolder != null && cursor != null) {
                //textViewNoData.setVisibility(View.INVISIBLE);
                cursor.moveToPosition(cursor.getPosition());
                holder.setData(cursor);
           /* } else {
                textViewNoData.setVisibility(View.VISIBLE);
            }*/
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return super.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView coverImageView;
        ImageView shareImageView;
        ImageView editImageView;
        ImageView deleteImageView;

        MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            coverImageView = v.findViewById(R.id.coverImageView);
            shareImageView = v.findViewById(R.id.shareImageView);
            editImageView = v.findViewById(R.id.editImageView);
            deleteImageView = v.findViewById(R.id.deleteImageView);

            editImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // The id we want to search for
                    String siteToFind = titleTextView.getText().toString();

                    // Holds the column data we want to retrieve
                    String[] projection = new String[]{"_id","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

                    // Pass the URL for Content Provider, the projection,
                    // the where clause followed by the matches in an array for the ?
                    // null is for sort order
                    @SuppressLint("Recycle")
                    Cursor foundSite = resolver.query(SitesProvider.CONTENT_URI, projection, "NOM = ? ", new String[]{siteToFind}, null);

                    // Cycle through our one result or print error
                    if(foundSite!=null){
                        if(foundSite.moveToFirst()){

                            int id = foundSite.getColumnIndex("_id ");
                            int id_ext = foundSite.getColumnIndex("ID_EXT");
                            String name = foundSite.getString(foundSite.getColumnIndex("NOM"));
                            double latitude = (double) foundSite.getColumnIndex("LATITUDE");
                            double longitude = (double) foundSite.getColumnIndex("LONGITUDE");
                            String adresse = foundSite.getString(foundSite.getColumnIndex("ADRESSE_POSTALE"));
                            String categorie = foundSite.getString(foundSite.getColumnIndex("CATEGORIE"));
                            String resume = foundSite.getString(foundSite.getColumnIndex("RESUME"));
                            byte[] image = foundSite.getBlob(foundSite.getColumnIndex("IMAGE"));

                            SiteData currentSite = new SiteData(id, id_ext, name, latitude, longitude, adresse, categorie, resume, image);

                            // Create new fragment, give it an object and start transaction
                            Fragment newFragment = new AjouterSiteDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("site", currentSite);
                            bundle.putString("latitude", foundSite.getString(foundSite.getColumnIndex("LATITUDE")));
                            bundle.putString("longitude", foundSite.getString(foundSite.getColumnIndex("LONGITUDE")));
                            newFragment.setArguments(bundle);

                            // consider using Java coding conventions (upper first char class names!!!)
                            FragmentTransaction transaction;
                            if (getFragmentManager() != null) {
                                transaction = getFragmentManager().beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();
                            }

                        } else {

                            Toast.makeText(getContext(), "Site introuvable", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(getContext(), "ERROR !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle("Supprimer le site " + titleTextView.getText().toString());
                    builder.setMessage("Êtes-vous sûr?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // The id we want to search for
                            String siteToDelete = titleTextView.getText().toString();

                            // Holds the column data we want to retrieve
                            String[] projection = new String[]{"_id","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

                            // Pass the URL for Content Provider, the projection,
                            // the where clause followed by the matches in an array for the ?
                            // null is for sort order
                            @SuppressLint("Recycle")
                            Cursor foundSite = resolver.query(SitesProvider.CONTENT_URI, projection, "NOM = ? ", new String[]{siteToDelete}, null);

                            // Cycle through our one result or print error
                            if(foundSite!=null) {
                                if (foundSite.moveToFirst()) {
                                    String id = foundSite.getString(foundSite.getColumnIndex("_id"));
                                    String URL1 = "content://com.example.sami.visitmetz_v2.ContentProvider.SitesProvider/sites_table/#" + id;
                                    Uri uri1 = Uri.parse(URL1);

                                    // Holds the column data we want to update
                                    String[] selectionargs = new String[]{"" + id};

                                    Toast.makeText(getActivity(), siteToDelete + " a été supprimé!", Toast.LENGTH_SHORT).show();

                                    // Use the resolver to delete ids by passing the content provider url
                                    // what you are targeting with the where and the string that replaces
                                    // the ? in the where clause
                                    resolver.delete(uri1,
                                            "_id = ? ", selectionargs);

                                    adapter.notifyDataSetChanged();


                                    // consider using Java coding conventions (upper first char class names!!!)
                                    FragmentTransaction transaction;
                                    if (getFragmentManager() != null) {
                                        transaction = getFragmentManager().beginTransaction();
                                        // Replace whatever is in the fragment_container view with this fragment,
                                        // and add the transaction to the back stack
                                        transaction.replace(R.id.fragment_container, new SitesOverviewFragment());
                                        transaction.addToBackStack(null);

                                        // Commit the transaction
                                        transaction.commit();
                                    }
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.create().show();
                }
            });


            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(coverImageView.getId())
                        + '/' + "drawable" + '/' + getResources().getResourceEntryName((int) coverImageView.getTag()));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                }
            });
        }

        public void setData(Cursor c) {
            if (c != null) {
                textViewNoData.setVisibility(View.INVISIBLE);
                byte[] img = c.getBlob(8);
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                titleTextView.setText(c.getString(c.getColumnIndex("NOM")));
                coverImageView.setImageBitmap(bitmap);
                coverImageView.setTag(bitmap);
                editImageView.setTag(R.drawable.edit_black_24dp);
                deleteImageView.setTag(R.drawable.ic_delete_black_24dp);
            } else {
                textViewNoData.setVisibility(View.VISIBLE);
            }

        }
    }
}
