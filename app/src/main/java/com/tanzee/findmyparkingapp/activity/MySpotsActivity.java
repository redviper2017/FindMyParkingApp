package com.tanzee.findmyparkingapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import com.tanzee.findmyparkingapp.R;
import com.tanzee.findmyparkingapp.adapters.MySpotsAdapters;
import com.tanzee.findmyparkingapp.db.MySpotsFrequency;
import com.tanzee.findmyparkingapp.utils.DBHelper;

public class MySpotsActivity extends AppCompatActivity {

    private ListView lv;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spots);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("My Spots");
        setSupportActionBar(toolbar);

        lv = (ListView) findViewById(R.id.lv_history);
        dbHelper = new DBHelper(this);

        lv.setAdapter(new MySpotsAdapters(this, dbHelper.getMySpotsAsListByFrequency()));

        List<MySpotsFrequency> k = dbHelper.getMySpotsAsListByFrequency();
        Log.e("freq size", k.get(0).getFrequency() + "");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
