package com.example.sami.visitmetz_v2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;
import com.example.sami.visitmetz_v2.models.PlaceInfo;
import com.example.sami.visitmetz_v2.models.SiteData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private GoogleMap mMap;

    SupportMapFragment mapFragment;

    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo, mPlacePicker, mRestaurant;
    public PlaceInfo  mPlace;
    public Marker mMarker,mMarkerB;
    private EditText mRoyen;
    private Button  mValide;
    private Spinner mSpinner;

    public DatabaseHelper dbh;



    //vars
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

   // ArrayList<MarkerOptions> collection2 = new ArrayList<MarkerOptions>();
   private LocationListener listener;
   private LocationManager locationManager;


    private static final int PLACE_PICKER_REQUEST = 1;

    int PROXIMITY_RADIUS = 200;

    double latitude, longitude;

    Location lastLoction = null;

    String PROVIDER_NAME = "com.example.sami.visitmetz_v2.ContentProvider.SitesProvider";
    String URL = "content://" + PROVIDER_NAME + "/sites_table";
    Uri uri = Uri.parse(URL);

    // Provides access to other applications Content Providers
    ContentResolver resolver;


    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-54.5247541978, 2.05338918702), new LatLng(9.56001631027, 51.1485061713));


    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mSearchText = (AutoCompleteTextView) v.findViewById(R.id.input_search);
        mGps = (ImageView) v.findViewById(R.id.ic_gps);
        mInfo = (ImageView) v.findViewById(R.id.place_info);
        mPlacePicker = (ImageView) v.findViewById(R.id.place_picker);

        mRoyen = (EditText)v.findViewById(R.id.input_cercle);
        mValide = (Button)v.findViewById(R.id.btn_valide);
        mSpinner = (Spinner) v.findViewById(R.id.spinner);



        /*  mRestaurant = (ImageView) v.findViewById(R.id.ic_restaurant); */

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.nav_map);

        resolver = getContext().getContentResolver();


        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.nav_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        getLocationPermission();
       // onLocationChanged();


       listener = new LocationListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onLocationChanged (Location location){

                Log.i(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx "  );

                Log.i(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " + location.getLatitude());
                Log.i(TAG, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq " + location.getLongitude());

                lastLoction  =  location;

                if(mRoyen.length() !=0 ){
                    mMap.clear();
                    drawCircle(new LatLng(location.getLatitude(),location.getLongitude()));
                    listMarker (new LatLng(location.getLatitude(),location.getLongitude()));
                }

            }

            @Override
            public void onStatusChanged (String s,int i, Bundle bundle){

            }

            @Override
            public void onProviderEnabled (String s){

            }

            @Override
            public void onProviderDisabled (String s){

            }

        };
        locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);


        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){

             locationManager.removeUpdates(listener);
        }
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), 0,this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this.getActivity(), mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }


                return false;
            }

        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
                mMap.clear();
                //onLocationChanged(lastLoction);

            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked place info : ");
                try{

                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d(TAG, "onClick: place info: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*    launches the place picker   */
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                }
            }
        });


        mValide.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                mMap.clear();
                // Drawing circle on the map
                drawCircle(new LatLng(latitude, longitude));
                listMarker (new LatLng(latitude,longitude));

            }

        }

        );

        hideSoftKeyboard();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void listMarker ( LatLng latlong){
        MarkerOptions options = new MarkerOptions();
        ArrayList<CustomMarker> liste = allMarkers();
        for (int i = 0; i < liste.size(); i++) {
            float results[] = new float[10];
            Location.distanceBetween(latlong.latitude, latlong.longitude, liste.get(i).getLat(), liste.get(i).getLongi(), results);
            if (Integer.parseInt(mRoyen.getText().toString()) > results[0]) {

                LatLng point = new LatLng(Double.valueOf(liste.get(i).getLat()), Double.valueOf(liste.get(i).getLongi()));
                options.position(point);
                options.title(liste.get(i).getName());
                options.snippet("Categorie: " + liste.get(i).getCategorie() + "  " + "Resumer: " + liste.get(i).getResumer());
                Log.d(TAG, "DIIIIIISTANCE :  " + results[0]);
                Log.d(TAG, "TTMMMMMmmm:  " + liste.get(i).getCategorie());

                if (mSpinner.getSelectedItem().toString().equals(liste.get(i).getCategorie())) {
                    Log.d(TAG, "RTTTTT" + mSpinner.getSelectedItem().toString());
                    Log.d(TAG, "RRRRRRRTTTTT" + liste.get(i).getCategorie());
                    mMap.addMarker(options);
                } else if (mSpinner.getSelectedItem().toString().equals("Tout")) {
                    mMap.addMarker(options);
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ArrayList<CustomMarker> allMarkers(){
        MarkerOptions options = new MarkerOptions();
        ArrayList<CustomMarker> collection = new ArrayList<CustomMarker>();

        // Projection contains the columns we want
        String[] projection = new String[]{"_ID", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE",
                "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

        // Pass the URL, projection and I'll cover the other options below
        Cursor dataCursor = resolver.query(uri, projection, null, null, null, null);
        //Toast.makeText(getActivity(),""+dataCursor.getCount(),Toast.LENGTH_LONG).show();

        while (dataCursor.moveToNext()) {
            int id = dataCursor.getColumnIndex("_id ");
            int id_ext = dataCursor.getColumnIndex("ID_EXT");
            String name = dataCursor.getString(dataCursor.getColumnIndex("NOM"));
            double lat = Double.parseDouble(dataCursor.getString(3));
            double longi = Double.parseDouble(dataCursor.getString(4));
            String adresse = dataCursor.getString(dataCursor.getColumnIndex("ADRESSE_POSTALE"));
            String categorie = dataCursor.getString(dataCursor.getColumnIndex("CATEGORIE"));
            String resume = dataCursor.getString(dataCursor.getColumnIndex("RESUME"));
            byte[] image = dataCursor.getBlob(dataCursor.getColumnIndex("IMAGE"));

            SiteData currentSite = new SiteData(id, id_ext, name, lat, longi, adresse, categorie, resume, image);
            LatLng point = new LatLng(currentSite.getLatitude(), currentSite.getLongitude());
            options.position(point);
            options.title(currentSite.getNom());
            options.snippet("Categorie: " + currentSite.getCategorie() + "  " + "Resume: " + currentSite.getResume());
            CustomMarker cMarker = new CustomMarker(currentSite.getCategorie(), currentSite.getLatitude(), currentSite.getLongitude(), currentSite.getNom(), currentSite.getResume());
            collection.add(cMarker);
        }
        return collection;
    }




    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

            circleOptions.center(point);


       // if(Integer.parseInt(mRoyen.getText().toString()) != 0){
        // Radius of the circle
         circleOptions.radius(Integer.parseInt(mRoyen.getText().toString()));

       // }else {
         //   circleOptions.radius(50);

      //  }

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void AddMarker (){

    }



    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(getActivity(), data);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }


    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
SpinnerItems();
            init();
          //  Toast.makeText(getActivity(),"Bla bla",Toast.LENGTH_LONG).show();

           //
        }



    }





    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation != null) {
                                latitude = currentLocation.getLatitude();

                            }

                            longitude = currentLocation.getLongitude();

                          //  lastLoction = currentLocation;

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "Moi !");

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }


    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear();
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.getActivity()));
        if(placeInfo != null){
            try{
                float results[] = new float[10];

                Location.distanceBetween(latitude, longitude, latLng.latitude , latLng.longitude, results);

                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Destination: " +   formatNumber(results[0]) + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);




                Toast.makeText(this.getActivity(), formatNumber(results[0]), Toast.LENGTH_LONG).show();
                //       Toast.makeText(this.getActivity(),item.getNomCard() , Toast.LENGTH_LONG).show();
            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideSoftKeyboard();
    }


    private String formatNumber(float distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    /* --------------------------------------------------------------------------------------------- */

   /*
    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch(v.getId())
        { */

          /*  case R.id.B_hopistals:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
                break;


            case R.id.B_schools:
                mMap.clear();
                String school = "school";
                url = getUrl(latitude, longitude, school);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Schools", Toast.LENGTH_SHORT).show();
                break; */
       /*     case R.id.ic_restaurant:
                mMap.clear();
                String resturant = "restuarant";
                String  url = getUrl(latitude, longitude, resturant);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getActivity(), "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
                break;

        }
    }
      */

 /*   private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
       // googlePlaceUrl.append("&keyword="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyD5k31sShLlOA_yZOMsEqJ5oy2SPB9ojBE");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    } */


    /* --------------------------------------------------------------------------------------------- */


    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("Moi !")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMarkerB =   mMap.addMarker(options);

        }
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.nav_map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.nav_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                //    initMap();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void SpinnerItems() {
        // Projection contains the columns we want
        String[] projection1 = new String[]{"_id", "nom"};

        // Pass the URL, projection and I'll cover the other options below
        Cursor data = getActivity().getContentResolver().query(CategoriesProvider.CONTENT_URI, projection1, null, null, null, null);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("-- Sélectionner une catégorie --");
        while(data.moveToNext())
        {
            categories.add(data.getString(data.getColumnIndex("nom")));
        }

        data.close();

        // Spinner click listener
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mSpinner.setAdapter(dataAdapter);
        dataAdapter.add("Tout");

    }


    /// ici pour évité le crache de l'app j'ai fais cette foction Quand on change fragment
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }





    /* --------------------------- google places API autocomplete suggestions -----------------*/

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
            places.release();
        }
    };


}