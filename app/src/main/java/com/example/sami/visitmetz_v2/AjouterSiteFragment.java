package com.example.sami.visitmetz_v2;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AjouterSiteFragment extends Fragment {

    final int REQUEST_CODE_GALLERY = 42;
    DatabaseHelper mDatabaseHelper1;
    Button bouton_ajouter_site;
    ImageButton bouton_choisir_image;
    EditText nom;
    EditText longitude;
    EditText latitude;
    EditText adresse_postale;
    EditText categorie;
    EditText resume;
    ImageView editImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ajouter_site_activity, container, false);
        bouton_ajouter_site = v.findViewById(R.id.bouton_ajouter_site);
        bouton_choisir_image = v.findViewById(R.id.bouton_choisir_image);
        editImage = v.findViewById(R.id.imageView4);
        nom = v.findViewById(R.id.nom);
        longitude = v.findViewById(R.id.longitude);
        latitude = v.findViewById(R.id.latitude);
        adresse_postale = v.findViewById(R.id.adresse_postale);
        categorie = v.findViewById(R.id.categorie);
        resume = v.findViewById(R.id.resume);


        mDatabaseHelper1 = new DatabaseHelper(this.getActivity());

        editImage.setVisibility(View.GONE);

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
                Double longSite = Double.valueOf(longitude.getText().toString());
                Double latSite = Double.valueOf(latitude.getText().toString());
                String adressSite = adresse_postale.getText().toString();
                String categorieSite = categorie.getText().toString();
                String resumeSite = resume.getText().toString();

                //Checks if it is not empty
                if (nom.length() != 0) {
                    AddData(nomSite, latSite, longSite, adressSite, categorieSite, resumeSite, ImageSite);
                    // Create new fragment and transaction
                    Fragment newFragment = new SitesOverviewFragment();
                    // consider using Java coding conventions (upper first char class names!!!)
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.nav_map, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                } else {
                    Toast("Le formulaire est vide !");
                }
            }

        });
        return v;
    }

    public void AddData(String name, Double latitude, Double longitude, String adresse, String categorie, String resume, byte[] image) {
        //Checks if the data is correctly added
        boolean insertData = mDatabaseHelper1.addData(name, latitude, longitude, adresse, categorie, resume, image);
        if(insertData)
        {
            Toast("Le site " + nom.getText() + " a bien été ajouté à 'Mes Sites' !");
        }
        else
        {
            Toast("Une erreur est survenue lors de l'ajout du site " + nom.getText() + " !");
        }
    }


    private void Toast(String s)
    {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private byte[] getByteFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }

    Intent intent;
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
                Toast.makeText(getActivity().getApplicationContext(), "Vous ne disposez pas d'autorisation pour mener cette action !", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                InputStream inputStream = this.getActivity().getContentResolver().openInputStream(uri);
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
