package com.tanzee.findmyparkingapp.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import corp.tz.findmyparking.R;
import corp.tz.findmyparking.dto.MapDirectionResponseDto;
import corp.tz.findmyparking.service.ApiService;
import corp.tz.findmyparking.utils.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private LatLng myPosition;
    private ApiService apiService;
    private PolylineOptions polylineOptions;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        apiService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            MarkerOptions options = new MarkerOptions()
                    .title("My Current Location")
                    .position(new LatLng(latitude, longitude));
            mMap.addMarker(options);

            LatLng ll = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    ll, 14);
            mMap.animateCamera(cameraUpdate);

            for(int t = 0; t < Constant.TOTAL_GEO; t++){
                if(t == 0){
                    double latitudeUB2 = Constant.UB_TWO_GEO_LAT;
                    double longitudeUB2 = Constant.UB_TWO_GEO_LNG;
                    MarkerOptions optionsUB2 = new MarkerOptions()
                            .title("BRAC University UB2")
                            .position(new LatLng(latitudeUB2, longitudeUB2));
                    mMap.addMarker(optionsUB2);
                }else if(t == 1){
                    double latitudeUB3 = Constant.UB_THREE_GEO_LAT;
                    double longitudeUB3 = Constant.UB_THREE_GEO_LNG;
                    MarkerOptions optionsUB3 = new MarkerOptions()
                            .title("BRAC University UB3")
                            .position(new LatLng(latitudeUB3, longitudeUB3));
                    mMap.addMarker(optionsUB3);
                }else if(t == 2){
                    double latitudeGausia = Constant.GAUSIA_AZOM_GEO_LAT;
                    double longitudeGausia = Constant.GAUSIA_AZOM_GEO_LNG;
                    MarkerOptions optionsGausia = new MarkerOptions()
                            .title("BRAC University UB3")
                            .position(new LatLng(latitudeGausia, longitudeGausia));
                    mMap.addMarker(optionsGausia);
                }
            }

            locateDistanceAndPath(Constant.TOTAL_GEO, location);

        }

        //String url = Constant.BASE_API_URL + location.getLatitude() + ", " + location.getLongitude() + "&destination=" + Constant.UB_TWO_GEO_LAT + ", " + Constant.UB_TWO_GEO_LNG;
        //Log.e("link", url);
        //locateDistanceAndPath(Constant.TOTAL_GEO, location);


    }

    private void locateDistanceAndPath(int totalGeo, Location location){

        final ArrayList<MapDirectionResponseDto.routes> list = new ArrayList<>();
        String url;
        for(int t = 0; t < totalGeo; t++){
            if(t == 0){
                url = Constant.BASE_API_URL + location.getLatitude() + ", " + location.getLongitude() + "&destination=" + Constant.UB_TWO_GEO_LAT + ", " + Constant.UB_TWO_GEO_LNG;
            }else if(t == 1){
                url = Constant.BASE_API_URL + location.getLatitude() + ", " + location.getLongitude() + "&destination=" + Constant.UB_THREE_GEO_LAT + ", " + Constant.UB_THREE_GEO_LNG;
            }else{
                url = Constant.BASE_API_URL + location.getLatitude() + ", " + location.getLongitude() + "&destination=" + Constant.GAUSIA_AZOM_GEO_LAT + ", " + Constant.GAUSIA_AZOM_GEO_LNG;
            }

            Log.e("dest", url);

            final int finalT = t;
            apiService.getDirection(url).enqueue(new Callback<MapDirectionResponseDto>(){
                @Override
                public void onResponse(Call<MapDirectionResponseDto> call, Response<MapDirectionResponseDto> response){
                    if(response.code() == HttpURLConnection.HTTP_OK){

                        list.add(response.body().getRoutes().get(0));

                        if(finalT == 2){

                            Log.e("total", list.size() + "");

                            polylineOptions = new PolylineOptions();
                            if(polyline != null){
                                polyline.remove();
                            }

                            Collections.sort(list, new Comparator<MapDirectionResponseDto.routes>() {
                                @Override
                                public int compare(MapDirectionResponseDto.routes z1, MapDirectionResponseDto.routes z2) {
                                    if (z1.getLegs().get(0).getD().getValue() > z2.getLegs().get(0).getD().getValue())
                                        return 1;
                                    if (z1.getLegs().get(0).getD().getValue() < z2.getLegs().get(0).getD().getValue())
                                        return -1;
                                    return 0;
                                }
                            });

                            Log.e("distance", response.body().getRoutes().get(0).getLegs().get(0).getD().getText());

                            if(list.get(0).getLegs().get(0).getSteps().size() > 0){
                                for(int t = 0; t < list.get(0).getLegs().get(0).getSteps().size(); t++){
                                    //Log.e("steps", response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLat() + "lat " + response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLng() + "lng");

//                                    double lat = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLat();
//                                    double lng = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLng();
//
//                                    double lat2 = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLat();
//                                    double lng2 = response.body().getRoutes().get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLng();

                                    double lat = list.get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLat();
                                    double lng = list.get(0).getLegs().get(0).getSteps().get(t).getStart_location().getLng();

                                    double lat2 = list.get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLat();
                                    double lng2 = list.get(0).getLegs().get(0).getSteps().get(t).getEnd_location().getLng();

                                    polylineOptions.add(new LatLng(lat, lng));
                                    polylineOptions.add(new LatLng(lat2, lng2));

                                }

                            }
                            polylineOptions.width(10);
                            polylineOptions.visible(true);
                            polylineOptions.zIndex(30);
                            polylineOptions.color(Color.BLUE);
                            polyline = mMap.addPolyline(polylineOptions);
                        }


                    }else{
                        Toast.makeText(MapsActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MapDirectionResponseDto> call, Throwable t){
                    Toast.makeText(MapsActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
