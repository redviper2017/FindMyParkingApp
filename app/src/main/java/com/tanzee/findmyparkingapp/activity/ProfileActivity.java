package com.tanzee.findmyparkingapp.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import corp.tz.findmyparking.R;
import corp.tz.findmyparking.databinding.ActivityProfileBinding;
import corp.tz.findmyparking.db.Profile;
import corp.tz.findmyparking.utils.DBHelper;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DBHelper dbHelper;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private Profile profile;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        dbHelper = new DBHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait");
        pd.show();

        if(firebaseAuth.getCurrentUser() != null){
            firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    pd.dismiss();
                    profile = dataSnapshot.getValue(Profile.class);
                    if(profile != null){
                        binding.container.etEmail.setText(profile.getEmail());
                        binding.container.etPhn.setText(profile.getPhone());
                        binding.container.etReg.setText(profile.getReg());
                        binding.container.etLic.setText(profile.getLic());
                        Glide.with(ProfileActivity.this).load(profile.getImage()).error(R.drawable.holder_one).into(binding.container.ivImage);
                    }else {
                        Toast.makeText(ProfileActivity.this, "Something went wrong, could not retrieve profile information", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError){
                    pd.dismiss();
                    if(firebaseAuth.getCurrentUser() != null){
                        profile = dbHelper.getProfileByEmail(firebaseAuth.getCurrentUser().getEmail());
                        if(profile != null){
                            binding.container.etEmail.setText(profile.getEmail());
                            binding.container.etPhn.setText(profile.getPhone());
                            binding.container.etReg.setText(profile.getReg());
                            binding.container.etLic.setText(profile.getLic());
                            Glide.with(ProfileActivity.this).load(profile.getImage()).into(binding.container.ivImage);
                        }else {
                            Toast.makeText(ProfileActivity.this, "Something went wrong, could not retrieve profile information", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }
            });
        }


    }

}
