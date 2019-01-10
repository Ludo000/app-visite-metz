package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;
import com.example.sami.visitmetz_v2.models.CategorieData;

import java.io.ByteArrayOutputStream;

public class CategoriesOverviewFragment extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView MyRecyclerView;
    MyAdapter adapter;
    TextView textViewNoData;
    Spinner spinner;

    private String newCategorie = "";
    private EditText nCategorie;

    ContentResolver resolver;

    EcouteurLoadEvenement_3 ecouteurLoadEvenement_3;

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
        spinner = view.findViewById(R.id.categorie_spinner);
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

        ecouteurLoadEvenement_3 = new EcouteurLoadEvenement_3(getContext(), adapter, null);

        getLoaderManager().initLoader(0, null, ecouteurLoadEvenement_3);
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
        ecouteurLoadEvenement_3 = new EcouteurLoadEvenement_3(getContext(), adapter, s.trim().length() > 0 ? s : null);

        getLoaderManager().restartLoader(0, null, ecouteurLoadEvenement_3);
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
        public CategoriesOverviewFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items_categorie, parent, false);
            return new CategoriesOverviewFragment.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
            CategoriesOverviewFragment.MyViewHolder holder = (CategoriesOverviewFragment.MyViewHolder) viewHolder;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView editImageView;
        ImageView deleteImageView;

        MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            editImageView = v.findViewById(R.id.editImageView);
            deleteImageView = v.findViewById(R.id.deleteImageView);


            editImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // The id we want to search for
                    String categorieToFind = titleTextView.getText().toString();

                    // Holds the column data we want to retrieve
                    String[] projection = new String[]{"_id","nom"};

                    // Pass the URL for Content Provider, the projection,
                    // the where clause followed by the matches in an array for the ?
                    // null is for sort order
                    @SuppressLint("Recycle")
                    Cursor foundCategorie = resolver.query(CategoriesProvider.CONTENT_URI, projection, "nom = ? ", new String[]{categorieToFind}, null);

                    // Cycle through our one result or print error
                    if(foundCategorie!=null){
                        if(foundCategorie.moveToFirst()){

                            int id = foundCategorie.getColumnIndex("_id ");
                            final String name = foundCategorie.getString(foundCategorie.getColumnIndex("nom"));

                            CategorieData currentCategorie = new CategorieData(id, name);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Modifier une catégorie");

                            // I'm using fragment here so I'm using getView() to provide ViewGroup
                            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_categorie, (ViewGroup) getView(), false);

                            // Inflate and set the layout for the dialog
                            // Pass null as the parent view because its going in the dialog layout
                            builder.setView(viewInflated);

                            // Set up the input
                            nCategorie = viewInflated.findViewById(R.id.newcategorie);
                            nCategorie.setText(currentCategorie.getName());

                            // Add action buttons
                            builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    newCategorie = nCategorie.getText().toString().trim();

                                    if (newCategorie.length() > 0) {
                                        ContentValues content = new ContentValues();
                                        content.put("nom", newCategorie);

                                        // Holds the column data we want to update
                                        String[] selectionargs = new String[]{name};

                                        int c = getActivity().getContentResolver().update(
                                                CategoriesProvider.CONTENT_URI, content, "nom = ?", selectionargs);
                                    } else {
                                        Toast.makeText(getContext(), "Le champ est vide!", Toast.LENGTH_LONG)
                                                .show();
                                        dialog.cancel();
                                    }
                                }
                            });

                            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();

                        } else {

                            Toast.makeText(getContext(), "Categorie introuvable", Toast.LENGTH_SHORT).show();

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
                    builder.setTitle("Supprimer la categorie " + titleTextView.getText().toString());
                    builder.setMessage("Êtes-vous sûr?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // The id we want to search for
                            String categorieToDelete = titleTextView.getText().toString();

                            // Holds the column data we want to retrieve
                            String[] projection = new String[]{"_id","nom"};

                            // Pass the URL for Content Provider, the projection,
                            // the where clause followed by the matches in an array for the ?
                            // null is for sort order
                            @SuppressLint("Recycle")
                            Cursor foundCategorie = resolver.query(CategoriesProvider.CONTENT_URI, projection, "nom = ? ", new String[]{categorieToDelete}, null);

                            // Cycle through our one result or print error
                            if(foundCategorie!=null) {
                                if (foundCategorie.moveToFirst()) {
                                    String id = foundCategorie.getString(foundCategorie.getColumnIndex("_id"));
                                    String URL1 = "content://com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider/categories_table/#" + id;
                                    Uri uri1 = Uri.parse(URL1);

                                    // Holds the column data we want to update
                                    String[] selectionargs = new String[]{"" + id};

                                    Toast.makeText(getActivity(), categorieToDelete + " a été supprimé!", Toast.LENGTH_SHORT).show();

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
                                        transaction.replace(R.id.fragment_container, new CategoriesOverviewFragment());
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
        }

        public void setData(Cursor c) {
            if (c != null) {
                textViewNoData.setVisibility(View.INVISIBLE);
                titleTextView.setText(c.getString(c.getColumnIndex("nom")));
                editImageView.setTag(R.drawable.edit_black_24dp);
                deleteImageView.setTag(R.drawable.ic_delete_black_24dp);
            } else {
                textViewNoData.setVisibility(View.VISIBLE);
            }

        }
    }
}
