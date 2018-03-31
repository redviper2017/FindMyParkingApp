package com.tanzee.findmyparkingapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import corp.tz.findmyparking.R;
import corp.tz.findmyparking.databinding.ActivityHomeBinding;
import corp.tz.findmyparking.db.MySpots;
import corp.tz.findmyparking.db.MySpotsFrequency;
import corp.tz.findmyparking.db.Profile;
import corp.tz.findmyparking.dto.MapDirectionResponseDto;
import corp.tz.findmyparking.service.ApiService;
import corp.tz.findmyparking.utils.Constant;
import corp.tz.findmyparking.utils.DBHelper;
import corp.tz.findmyparking.utils.RouteTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SensorEventListener {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Button mapBtn;
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle mDrawerToggle;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private double currentLatitude;
    private double currentLongitude;
    private boolean IS_MAP_READY = false;
    private GoogleMap mMap;
    private Location location;

    private ApiService apiService;
    private PolylineOptions polylineOptions;
    private Polyline polyline;

    private boolean canLocationCalculated = false;

    private int azimut = 0;
    float[] mGravity;
    float[] mGeomagnetic;
    private Bitmap icon;
    private BitmapDescriptor iconDes;
    private LatLng mLastLatLng;
    private Handler handler;
    private Runnable updateTask;
    private boolean mapAnimation = false;
    private double ORIGIN_LAT;
    private double ORIGIN_LNG;
    private SensorManager sensorManager;
    private Marker origin;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private Profile profile;
    private DBHelper dbHelper;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setMessage("Please wait");

        mapBtn = (Button) findViewById(R.id.goToMap);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        //initiateHandler();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrow);
        icon = Bitmap.createScaledBitmap(icon, 80, 80, false);
        iconDes = BitmapDescriptorFactory.fromBitmap(icon);

        apiService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        binding.shitstuff.setItemIconTintList(null);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        View v = binding.shitstuff.getHeaderView(0);
        final TextView name = (TextView) v.findViewById(R.id.tv_name);
        final TextView number = (TextView) v.findViewById(R.id.tv_number);
        final TextView email = (TextView) v.findViewById(R.id.tv_country);
        final ImageView iv = (ImageView) v.findViewById(R.id.iv_image);

        if(firebaseAuth.getCurrentUser() != null){
            firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    profile = dataSnapshot.getValue(Profile.class);
                    if(profile != null){
                        name.setText(profile.getName());
                        email.setText(profile.getEmail());
                        number.setText(profile.getPhone());
                        Glide.with(HomeActivity.this).load(profile.getImage()).error(R.drawable.holder_one).into(iv);
                    }else {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError){
                    if(firebaseAuth.getCurrentUser() != null){
                        profile = dbHelper.getProfileByEmail(firebaseAuth.getCurrentUser().getEmail());
                        if(profile != null){
                            name.setText(profile.getName());
                            email.setText(profile.getEmail());
                            number.setText(profile.getPhone());
                            Glide.with(HomeActivity.this).load(profile.getImage()).error(R.drawable.holder_one).into(iv);
                        }else {

                        }
                    }
                }
            });
        }

        binding.shitstuff.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                if(item.getItemId() == R.id.action_history){
                    binding.drawerLayout.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                }else if(item.getItemId() == R.id.action_profile){
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                }else if(item.getItemId() == R.id.action_signout){
                    firebaseAuth.signOut();
                    binding.drawerLayout.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }else if(item.getItemId() == R.id.action_places){
                    startActivity(new Intent(HomeActivity.this, MySpotsActivity.class));
                }
                return false;
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(canLocationCalculated){
                    if(location != null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        MarkerOptions options = new MarkerOptions()
                                .title("My Current Location")
                                .position(new LatLng(latitude, longitude));
                        mMap.addMarker(options);

                        LatLng ll = new LatLng(latitude, longitude);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                ll, Constant.MAP_ZOOM_LEVEL);
                        mapAnimation = false;
                        mMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback(){
                            @Override
                            public void onFinish(){
                                mapAnimation = true;
                            }

                            @Override
                            public void onCancel(){
                                mapAnimation = true;
                            }

                        });

                        ArrayList<String> points = Constant.getParkingPoint();

                        for(int t = 0; t < points.size(); t++){
                            String s = points.get(t);
                            String sArr [] = s.split(",");
                            LatLng latLng = new LatLng(Double.parseDouble(sArr[0]), Double.parseDouble(sArr[1]));
                            MarkerOptions optionsUB2 = new MarkerOptions()
                                    .title("Parking Spot " + t)
                                    .position(latLng);
                            mMap.addMarker(optionsUB2);
//                            if(t == 0){
//                                double latitudeUB2 = Constant.UB_TWO_GEO_LAT;
//                                double longitudeUB2 = Constant.UB_TWO_GEO_LNG;
//                                MarkerOptions optionsUB2 = new MarkerOptions()
//                                        .title("BRAC University UB2")
//                                        .position(new LatLng(latitudeUB2, longitudeUB2));
//                                mMap.addMarker(optionsUB2);
//                            }else if(t == 1){
//                                double latitudeUB3 = Constant.UB_THREE_GEO_LAT;
//                                double longitudeUB3 = Constant.UB_THREE_GEO_LNG;
//                                MarkerOptions optionsUB3 = new MarkerOptions()
//                                        .title("BRAC University UB3")
//                                        .position(new LatLng(latitudeUB3, longitudeUB3));
//                                mMap.addMarker(optionsUB3);
//                            }else if(t == 2){
//                                double latitudeGausia = Constant.GAUSIA_AZOM_GEO_LAT;
//                                double longitudeGausia = Constant.GAUSIA_AZOM_GEO_LNG;
//                                MarkerOptions optionsGausia = new MarkerOptions()
//                                        .title("BRAC University UB3")
//                                        .position(new LatLng(latitudeGausia, longitudeGausia));
//                                mMap.addMarker(optionsGausia);
//                            }
                        }

                        locateDistanceAndPath(Constant.TOTAL_GEO, location, points);

                    }
                }else{
                    Toast.makeText(HomeActivity.this, "Map Is Not Ready Yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onLocationChanged(Location location){
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        mLastLatLng = new LatLng(currentLatitude, currentLongitude);
        ORIGIN_LAT = location.getLatitude();
        ORIGIN_LNG = location.getLongitude();
        origin = mMap.addMarker(new MarkerOptions().position(mLastLatLng).icon(iconDes).title("Current Location").draggable(false));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLastLatLng)
                .zoom(Constant.MAP_ZOOM_LEVEL)   // Sets the zoom level
                .bearing(0)                       // Sets the orientation of the camera to east
                .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                .build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback(){
            @Override
            public void onFinish(){
                mapAnimation = true;
                Log.e("animation", "finished");
            }

            @Override
            public void onCancel(){
                mapAnimation = true;
            }

        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.REQUEST_ACCESSFINELOCATION);
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            readLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        if(connectionResult.hasResolution()){
            try{
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            }catch(IntentSender.SendIntentException e){
                // Log the error
                e.printStackTrace();
            }
        }else{
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        IS_MAP_READY = true;
        this.mMap = googleMap;

    }

    @Override
    protected void onResume(){
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mMap.clear();
        sensorManager.unregisterListener(this);

    }

    private void readLocation(Location location){
        //If everything went fine lets get latitude and longitude
        mapAnimation = false;
        mMap.setMyLocationEnabled(true);
        ORIGIN_LAT = location.getLatitude();
        ORIGIN_LNG = location.getLongitude();
        canLocationCalculated = true;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        //loadHome(currentLatitude, currentLongitude);
        Log.e("Error", "" + currentLatitude + " " + currentLongitude);
        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

        if(IS_MAP_READY){
            LatLng sydney = new LatLng(currentLatitude, currentLongitude);
//            mMap.addMarker(new MarkerOptions().position(sydney)
//                    .title("Starting Point"));

            if(origin != null){
                origin.remove();
            }
            origin = mMap.addMarker(new MarkerOptions().position(sydney).icon(iconDes).title("Current Location").draggable(false));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney)
                    .zoom(Constant.MAP_ZOOM_LEVEL)   // Sets the zoom level
                    .bearing(0)                       // Sets the orientation of the camera to east
                    .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                    .build();
            //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback(){
                @Override
                public void onFinish(){
                    mapAnimation = true;
                    Log.e("animation", "finished");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            initiateHandler();
                        }
                    }, 2500);

                }

                @Override
                public void onCancel(){
                    mapAnimation = true;
                }

            });

        }
    }

    private void locateDistanceAndPath(int totalGeo, Location location, final ArrayList<String> points){

        pd.show();
        final ArrayList<RouteTracker> list = new ArrayList<>();

        String url;
        for(int t = 0; t < points.size(); t++){

            String s = points.get(t);
            String sArr [] = s.split(",");
            url = Constant.BASE_API_URL + location.getLatitude() + ", " + location.getLongitude() + "&destination=" + sArr[0] + ", " + sArr[1] + "&key=AIzaSyDlXtKoCIB9RtQzKn_ubMjtd-Rf4E85W-Q";

            Log.e("dest", url);

            final int finalT = t;
            final String g = url;
            apiService.getDirection(url).enqueue(new Callback<MapDirectionResponseDto>(){
                @Override
                public void onResponse(Call<MapDirectionResponseDto> call, Response<MapDirectionResponseDto> response){
                    if(response.code() == HttpURLConnection.HTTP_OK){
                        RouteTracker routeTracker = new RouteTracker(response.body().getRoutes().get(0), finalT);
                        list.add(routeTracker);

                        //if(finalT == 2){

                            Log.e("total", list.size() + "");

                            polylineOptions = new PolylineOptions();
                            if(polyline != null){
                                polyline.remove();
                            }

                            Collections.sort(list, new Comparator<RouteTracker>(){
                                @Override
                                public int compare(RouteTracker z1, RouteTracker z2){
                                    if(z1.getRoutes().getLegs().get(0).getD().getValue() > z2.getRoutes().getLegs().get(0).getD().getValue())
                                        return 1;
                                    if(z1.getRoutes().getLegs().get(0).getD().getValue() < z2.getRoutes().getLegs().get(0).getD().getValue())
                                        return -1;
                                    return 0;
                                }
                            });

                            if(list.get(0).getRoutes().getLegs().get(0).getSteps().size() > 0){
                                Log.e("distance", "drawn");
                                for(int t = 0; t < list.get(0).getRoutes().getLegs().get(0).getSteps().size(); t++){
                                    //Log.e("steps", response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLat() + "lat " + response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLng() + "lng");

//                                    double lat = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLat();
//                                    double lng = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLng();
//
//                                    double lat2 = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLat();
//                                    double lng2 = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLng();

                                    double lat = list.get(0).getRoutes().getLegs().get(0).getSteps().get(t).getStart_location().getLat();
                                    double lng = list.get(0).getRoutes().getLegs().get(0).getSteps().get(t).getStart_location().getLng();

                                    double lat2 = list.get(0).getRoutes().getLegs().get(0).getSteps().get(t).getEnd_location().getLat();
                                    double lng2 = list.get(0).getRoutes().getLegs().get(0).getSteps().get(t).getEnd_location().getLng();

                                    polylineOptions.add(new LatLng(lat, lng));
                                    polylineOptions.add(new LatLng(lat2, lng2));

                                }

                            }
                            polylineOptions.width(10);
                            polylineOptions.visible(true);
                            polylineOptions.zIndex(30);
                            polylineOptions.color(Color.parseColor(getResources().getString(R.color.colorPrimary)));
                            polyline = mMap.addPolyline(polylineOptions);
                        //}


                    }else{
                        Toast.makeText(HomeActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }

                    if(finalT == points.size() - 1){
                        pd.dismiss();
                        SimpleDateFormat sdf = new SimpleDateFormat("EE hh:mm a, yyyy/MM/dd");
                        String currentDateandTime = sdf.format(new Date());

                        MySpots mySpots = new MySpots();
                        mySpots.setDate(currentDateandTime);
                        mySpots.setName(list.get(0).getRoutes().getLegs().get(0).getEnd_address());
                        mySpots.setOrigin(currentLatitude + "," + currentLongitude);
                        mySpots.setDestination(points.get(list.get(0).getTracker()));
                        mySpots.setTimeInLong(new Date().getTime());

                        MySpotsFrequency mySpotsFrequency = new MySpotsFrequency();
                        mySpotsFrequency.setDate(currentDateandTime);
                        mySpotsFrequency.setName(list.get(0).getRoutes().getLegs().get(0).getEnd_address());
                        mySpotsFrequency.setOrigin(currentLatitude + "," + currentLongitude);
                        mySpotsFrequency.setDestination(points.get(list.get(0).getTracker()));
                        mySpotsFrequency.setTimeInLong(new Date().getTime());

                        dbHelper.inserMySpots(mySpots);
                        dbHelper.insertMySpotsByFrequency(mySpotsFrequency);

                        Log.e("time", dbHelper.getMySpotsAsList().get(0).getFrequency() + " " + dbHelper.getMySpotsAsList().get(0).getDestination());
                    }
                }

                @Override
                public void onFailure(Call<MapDirectionResponseDto> call, Throwable t){
                    if(finalT == points.size() - 1){
                        pd.dismiss();
                    }
                    Toast.makeText(HomeActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constant.REQUEST_ACCESSFINELOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readLocation(location);
            }else{
                Toast.makeText(this, "You Must Have To Allow These Permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
            //initiateHandler();
        }
    }

    private void initiateHandler(){
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.e("Permission error", "You have permission");
                handler = new Handler();
                updateTask = new Runnable(){
                    @Override
                    public void run(){
                        updateCamera(azimut);
                        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, HomeActivity.this);
                        if(handler != null){
                            handler.postDelayed(this, 1000);
                        }
                    }
                };
                handler.postDelayed(updateTask, 1000);
            }else{

                Log.e("Permission error", "You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                //return false;
            }
        }else{ //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission");
            handler = new Handler();
            updateTask = new Runnable(){
                @Override
                public void run(){
                    updateCamera(azimut);
                    //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, HomeActivity.this);
                    if(handler != null){
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(updateTask, 1000);
        }

    }

    private void updateCamera(float bearing){
        if(mapAnimation == true){
            CameraPosition oldPos = mMap.getCameraPosition();
            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
            mapAnimation = false;
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), new GoogleMap.CancelableCallback(){
                @Override
                public void onFinish(){
                    mapAnimation = true;
                }

                @Override
                public void onCancel(){
                    mapAnimation = true;
                }

            });
        }

        Log.e("updated", "updated");

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if(mGravity != null && mGeomagnetic != null){
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if(success){
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = (int) Math.round(Math.toDegrees(orientation[0]));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
}
