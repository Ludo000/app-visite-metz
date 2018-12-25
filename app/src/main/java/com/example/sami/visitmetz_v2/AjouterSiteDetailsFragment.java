package com.example.sami.visitmetz_v2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.models.SiteData;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AjouterSiteDetailsFragment extends Fragment {

    final int REQUEST_CODE_GALLERY = 42;
    DatabaseHelper mDatabaseHelper1;
    Button bouton_ajouter_site, btnAnnuler;
    ImageButton bouton_choisir_image;
    EditText nom;
    EditText longitude;
    EditText latitude;
    EditText adresse_postale;
    EditText categorie;
    EditText resume;
    ImageView editImage;
    Bitmap bitmap;
    String oldName;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ajouter_site_activity, container, false);
        bouton_ajouter_site = v.findViewById(R.id.bouton_ajouter_site);
        bouton_choisir_image = v.findViewById(R.id.bouton_choisir_image);

        Bundle bundle = getArguments();

        SiteData site = null;
        if (bundle != null) {
            site = (SiteData) bundle.getSerializable("site");
        }

        editImage = v.findViewById(R.id.imageView4);
        longitude = v.findViewById(R.id.longitude);
        latitude = v.findViewById(R.id.latitude);
        adresse_postale = v.findViewById(R.id.adresse_postale);
        categorie = v.findViewById(R.id.categorie);
        resume = v.findViewById(R.id.resume);
        editImage.setVisibility(View.VISIBLE);
        nom = v.findViewById(R.id.nom);

        if (site != null) {
            bitmap = BitmapFactory.decodeByteArray(site.getImage(), 0, site.getImage().length);
            editImage.setImageBitmap(bitmap);
            nom.setText(site.getNom());
            oldName = site.getNom();
            longitude.setText(bundle.getString("longitude"));
            latitude.setText(bundle.getString("latitude"));
            adresse_postale.setText(site.getAdresse());
            categorie.setText(site.getCategorie());
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
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.fragment_container, newFragment);
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
                bouton_choisir_image.setVisibility(View.GONE);
            }
        });

        //When the button is clicked, the button in the text field is added to the database
        bouton_ajouter_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomSite = nom.getText().toString();
                byte[] ImageSite = getByteFromDrawable(editImage.getDrawable());
                double longSite = Double.valueOf(longitude.getText().toString());
                double latSite = Double.valueOf(latitude.getText().toString());
                String adressSite = adresse_postale.getText().toString();
                String categorieSite = categorie.getText().toString();
                String resumeSite = resume.getText().toString();

                //Checks if it is not empty
                if (nomSite.trim().length() > 0) {

                    String PROVIDER_NAME = "com.example.sami.visitmetz_v2.ContentProvider.SitesProvider";
                    String URL = "content://" + PROVIDER_NAME + "/sites_table";
                    Uri uri = Uri.parse(URL);

                    // Holds the column data we want to retrieve
                    String[] projection = new String[]{"_ID","ID_EXT", "NOM", "LATITUDE", "LONGITUDE", "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

                    // Pass the URL for Content Provider, the projection,
                    // the where clause followed by the matches in an array for the ?
                    // null is for sort order
                    @SuppressLint("Recycle")
                    Cursor foundSite = getContext().getContentResolver().query(uri, projection, "NOM = ? ", new String[]{oldName}, null);


                    //update a site
                    ContentValues sitesValues = contentValues(0, nomSite, latSite, longSite, adressSite, categorieSite, resumeSite, ImageSite);

                    // Cycle through our one result or print error
                    if(foundSite!=null) {
                        if (foundSite.moveToFirst()) {

                            String id = foundSite.getString(foundSite.getColumnIndex("_ID"));
                            String URL1 = "content://" + PROVIDER_NAME + "/sites_table/#" + id;
                            Uri uri1 = Uri.parse(URL1);

                            // Holds the column data we want to update
                            String[] selectionargs = new String[]{""+id};

                            int c = getContext().getContentResolver().update(
                                    uri1, sitesValues, "_ID = ?", selectionargs);

                            Toast.makeText(getContext(), c +" site a été modifié", Toast.LENGTH_LONG)
                                    .show();

                            // Create new fragment and transaction
                            Fragment newFragment = new SitesOverviewFragment();
                            // consider using Java coding conventions (upper first char class names!!!)
                            FragmentTransaction transaction;
                            if (getFragmentManager() != null) {
                                transaction = getFragmentManager().beginTransaction();
                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();
                            }
                        }
                    }
                } else {
                    Toast("Le formulaire est vide !");
                }
            }
        });
        return v;
    }

    public ContentValues contentValues(int id_ext, String nom, double latitude, double longitude, String adresse, String categorie, String resume, byte[] image)
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
        values.put("categorie",categorie);
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
}
