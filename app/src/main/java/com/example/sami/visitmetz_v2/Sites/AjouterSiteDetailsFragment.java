package com.example.sami.visitmetz_v2.Sites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;
import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.DatabaseHelper;
import com.example.sami.visitmetz_v2.R;
import com.example.sami.visitmetz_v2.models.SiteData;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AjouterSiteDetailsFragment extends Fragment {

    final int REQUEST_CODE_GALLERY = 42;
    DatabaseHelper mDatabaseHelper1;
    Button bouton_modifier_site, btnAnnuler, bouton_ajouter_categorie;
    ImageButton bouton_choisir_image;
    EditText nom;
    EditText longitude;
    EditText latitude;
    EditText adresse_postale;
    EditText resume;
    ImageView editImage;
    Bitmap bitmap;

    String oldName;

    Spinner spinner;

    private String newCategorie = "";
    private EditText nCategorie;
    List<String> categories;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ajouter_site_details_activity, container, false);
        bouton_modifier_site = v.findViewById(R.id.bouton_ajouter_site);
        bouton_choisir_image = v.findViewById(R.id.bouton_choisir_image);
        bouton_ajouter_categorie = v.findViewById(R.id.bouton_ajouter_categorie);
        spinner = v.findViewById(R.id.categorie_spinner);
        loadspinner();

        Bundle bundle = getArguments();

        SiteData site = null;
        if (bundle != null) {
            site = (SiteData) bundle.getSerializable("site");
        }

        editImage = v.findViewById(R.id.imageView4);
        longitude = v.findViewById(R.id.longitude);
        latitude = v.findViewById(R.id.latitude);
        adresse_postale = v.findViewById(R.id.adresse_postale);
        resume = v.findViewById(R.id.resume);
        nom = v.findViewById(R.id.nom);

        if (site != null) {
            bitmap = BitmapFactory.decodeByteArray(site.getImage(), 0, site.getImage().length);
            editImage.setImageBitmap(bitmap);
            editImage.setTag(bitmap);
            nom.setText(site.getNom());
            oldName = site.getNom();

            String[] projectionCategorie = new String[]{"_idCategorie","nom"};

            String[] selectionargCategorie = new String[]{""+site.getIdCategorie()};
            Toast.makeText(getContext(), ""+site.getIdCategorie(), Toast.LENGTH_SHORT).show();

            @SuppressLint("Recycle")
            Cursor categorie = getContext().getContentResolver().query(CategoriesProvider.CONTENT_URI, projectionCategorie, "_idCategorie = ?", selectionargCategorie, null);
            if(categorie!=null) {
                if (categorie.moveToFirst()) {
                    String nomCategorie = categorie.getString(categorie.getColumnIndex("nom"));
                    Toast.makeText(getContext(), nomCategorie, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < categories.size(); i++) {
                        if (nomCategorie.equals(categories.get(i))){
                            spinner.setSelection(i);
                            Log.d("RRRR ==> ",nomCategorie);
                        }
                    }
                } else {
                    Log.d("#### ==> ","Categorie introuvable!");
                }
            } else {
                Log.d("#### ==> ","ERROR Categorie !");
            }

            longitude.setText(""+site.getLongitude());
            latitude.setText(""+site.getLatitude());
            adresse_postale.setText(site.getAdresse());
            resume.setText(site.getResume());
        }

        btnAnnuler = v.findViewById(R.id.bouton_annuler);
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment, give it an object and start transaction
                Fragment newFragment = new SitesOverviewFragment();

                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = null;
                if (getFragmentManager() != null) {
                    transaction = getFragmentManager().beginTransaction();
                }
                if (transaction != null) {
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    for (Fragment fragment:getFragmentManager().getFragments()) {
                        if (fragment instanceof AjouterSiteDetailsFragment) {
                            getFragmentManager().beginTransaction().remove(fragment).commit();
                        }
                    }
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                }
            }
        });

        mDatabaseHelper1 = new DatabaseHelper(this.getActivity());

        bouton_choisir_image.setVisibility(View.VISIBLE);
        bouton_choisir_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_CODE_GALLERY);
                //bouton_choisir_image.setVisibility(View.GONE);
            }
        });

        bouton_ajouter_categorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ajouter une nouvelle catégorie");

                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_categorie, (ViewGroup) getView(), false);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(viewInflated);

                // Set up the input
                nCategorie = viewInflated.findViewById(R.id.newcategorie);

                // Add action buttons
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        newCategorie = nCategorie.getText().toString().trim();

                        if (newCategorie.length() > 0) {
                            //On cherche si duplica
                            String[] projection = new String[]{"_idCategorie","nom"};
                            @SuppressLint("Recycle")
                            Cursor foundSite = getContext().getContentResolver().query(CategoriesProvider.CONTENT_URI, projection, "nom = ?", new String[]{newCategorie}, null);

                            if(foundSite!=null) {
                                if (foundSite.moveToFirst()) {
                                    Toast.makeText(getContext(), "Une categorie avec le nom '"+ newCategorie+"' existe déjà!", Toast.LENGTH_LONG).show();
                                } else {
                                    ContentValues content = new ContentValues();
                                    content.put("nom", newCategorie);

                                    Uri uri2 = getActivity().getContentResolver().insert(
                                            CategoriesProvider.CONTENT_URI, content);
                                    loadspinner();
                                    Toast.makeText(getContext(), "La catégorie '"+ newCategorie+"' a été ajoutée!", Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Le champ est invalide!", Toast.LENGTH_LONG)
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
            }
        });

        //When the button is clicked, the button in the text field is added to the database
        bouton_modifier_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomSite = nom.getText().toString().trim();
                byte[] ImageSite = getByteFromDrawable(editImage.getDrawable());
                double longSite = Double.parseDouble(longitude.getText().toString());
                double latSite = Double.parseDouble(latitude.getText().toString());
                String adressSite = adresse_postale.getText().toString().trim();
                String categorieSite = spinner.getSelectedItem().toString().trim();
                String resumeSite = resume.getText().toString().trim();

                //Checks if it is not empty
                if (nomSite.length() > 0 && latSite > 0 && longSite > 0) {

                    //On cherche si duplica
                    String[] projection = new String[]{"_id", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "_idCategorie", "RESUME", "IMAGE"};

                    // Holds the column data we want to update
                    String[] selectionargs = new String[]{oldName};

                    @SuppressLint("Recycle")
                    Cursor foundSite = getContext().getContentResolver().query(SitesProvider.CONTENT_URI, projection, "NOM = ? AND LATITUDE=? AND LONGITUDE=? ", new String[]{nomSite, Double.toString(latSite), Double.toString(longSite)}, null);

                    if (foundSite != null) {
                        if (foundSite.moveToFirst()) {
                            Toast.makeText(getContext(), "Un site avec le nom '" + nomSite + "' existe déjà!", Toast.LENGTH_LONG).show();
                        } else {

                            String[] projectionCategorie = new String[]{"_idCategorie","nom"};

                            String[] selectionargCategorie = new String[]{categorieSite};

                            @SuppressLint("Recycle")
                            Cursor categorie = getContext().getContentResolver().query(CategoriesProvider.CONTENT_URI, projectionCategorie, "nom = ?", selectionargCategorie, null);
                            if(categorie!=null) {
                                if (categorie.moveToFirst()) {
                                    int idCategorie = Integer.parseInt(categorie.getString(categorie.getColumnIndex("_idCategorie")));
                                    // Add a new site record
                                    ContentValues sitesValues = contentValues(0, nomSite, latSite, longSite, adressSite, idCategorie, resumeSite, ImageSite);

                                    int i = getContext().getContentResolver().update(
                                            SitesProvider.CONTENT_URI, sitesValues, "NOM = ?", selectionargs);

                                    Toast.makeText(getContext(), "Le site a été modifié!", Toast.LENGTH_LONG)
                                            .show();


                                    FragmentTransaction transaction;
                                    if (getFragmentManager() != null) {
                                        transaction = getFragmentManager().beginTransaction();

                                        for (Fragment fragment:getFragmentManager().getFragments()) {
                                            if (fragment instanceof AjouterSiteDetailsFragment) {
                                                getFragmentManager().beginTransaction().remove(fragment).commit();
                                            }
                                        }
                                        transaction.addToBackStack(null);

                                        // Commit the transaction
                                        transaction.commit();
                                    }
                                }
                            }
                        }
                    } else {Toast.makeText(getContext(), "ERROR!!!", Toast.LENGTH_LONG)
                            .show();}
                } else {
                Toast.makeText(getContext(), "Le formulaire est invalide!", Toast.LENGTH_LONG)
                        .show();
                }
            }
        });
        return v;
    }

    public ContentValues contentValues(int id_ext, String nom, double latitude, double longitude, String adresse, int _idCategorie, String resume, byte[] image)
    {
        //Opens the database that will be used for writing and reading
        mDatabaseHelper1.getWritableDatabase();
        //Permits to add new info in the table
        ContentValues values = new ContentValues();
        values.put("id_ext",id_ext);
        values.put("nom",nom);
        values.put("image",image);
        values.put("latitude",latitude);
        values.put("longitude",longitude);
        values.put("adresse_postale",adresse);
        values.put("_idCategorie",_idCategorie);
        values.put("resume",resume);
        return values;
    }

    private void Toast(String s)
    {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @NonNull
    public byte[] getByteFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }

    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("image/*");

                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast("Vous ne disposez pas d'autorisation pour mener cette action !");
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = Objects.requireNonNull(this.getActivity()).getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editImage.setVisibility(View.VISIBLE);
                editImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadspinner(){

        // Projection contains the columns we want
        String[] projection1 = new String[]{"_idCategorie", "nom"};

        // Pass the URL, projection and I'll cover the other options below
        Cursor data = getActivity().getContentResolver().query(CategoriesProvider.CONTENT_URI, projection1, null, null, null, null);

        // Spinner Drop down elements
        categories = new ArrayList<>();
        categories.add("-- Sélectionner une catégorie --");
        while(data.moveToNext())
        {
            categories.add(data.getString(data.getColumnIndex("nom")));
        }

        data.close();

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
}
