package com.tanzee.findmyparkingapp.utils;


import java.util.ArrayList;

public class Constant{

    public static final int TOTAL_GEO = 3;

    //0
    public static final double UB_TWO_GEO_LAT = 23.780170;
    public static final double UB_TWO_GEO_LNG = 90.407225;

    //1
    public static final double UB_THREE_GEO_LAT = 23.7802827;
    public static final double UB_THREE_GEO_LNG = 90.4072158;

    //2
    public static final double GAUSIA_AZOM_GEO_LAT = 23.7805906;
    public static final double GAUSIA_AZOM_GEO_LNG = 90.4092380;



    public static final float MAP_ZOOM_LEVEL = 18;


    //Direction api
    public static String BASE_URL = "https://maps.googleapis.com/";
    //Direction api
    public static String BASE_API_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";

    //Private Server Api link
    public static String BASE_URL_SERVER = "http://find-my-parking.000webhostapp.com/";

    public static final int FB_ACCOUNTLIT_ACTIVITY_RESULT_CODE = 7;

    public static final int REQUEST_ACCESSFINELOCATION = 1;

    public static ArrayList<String> getParkingPoint(){
        ArrayList<String> points = new ArrayList<>();

        points.add("23.780170,90.407225");
        points.add("23.7802827,90.4072158");
        points.add("23.7805906,90.4092380");
        points.add("23.765090,90.358349");
        points.add("23.751404,90.390851");
        points.add("23.813813,90.424372");
        points.add("23.736267,90.384360");
        points.add("23.738440,90.377036");
        points.add("23.790879,90.405197");
        points.add("23.758423,90.373960");
        points.add("23.849089,90.405918");
        points.add("23.775056,90.365637");
        points.add("23.813100,90.430470");
        points.add("23.750886,90.393958");

        return points;
    }

}
