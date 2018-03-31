package com.tanzee.findmyparkingapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import corp.tz.findmyparking.R;
import corp.tz.findmyparking.databinding.ActivityRegisterBinding;
import corp.tz.findmyparking.db.Profile;
import corp.tz.findmyparking.utils.DBHelper;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
//import siclo.com.ezphotopicker.api.EZPhotoPick;
//import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
//import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
//import siclo.com.ezphotopicker.api.models.PhotoSource;

public class RegisterActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText nameTextField;
    private EditText emailAddressTextField;
    private EditText passwordTextField;
    private Button registerButton;
    private Button alreadyRegistered;
    private ProgressDialog pd;
    private Bitmap profilePhoto;

    private DatabaseReference databaseReference;

    private ActivityRegisterBinding binding;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        dbHelper = new DBHelper(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);

        profilePhoto = BitmapFactory.decodeResource(getResources(), R.drawable.holder_one);

        nameTextField = (EditText) findViewById(R.id.name);
        emailAddressTextField = (EditText) findViewById(R.id.email);
        passwordTextField = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.btnRegister);
        alreadyRegistered = (Button) findViewById(R.id.btnLinkToLoginScreen);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        nameTextField = (EditText) findViewById(R.id.name);


        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!binding.email.getText().toString().equals("")){
                    if(!binding.password.getText().toString().equals("")){
                        if(!binding.name.getText().toString().equals("")){
                            if(!binding.etPhn.getText().toString().equals("")){
                                if(!binding.etReg.getText().toString().equals("")){
                                    if(!binding.etLic.getText().toString().equals("")){
                                        pd.show();
                                        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                                                    @Override
                                                    public void onSuccess(final AuthResult authResult){

                                                        final Profile profile = new Profile();
                                                        profile.setEmail(binding.email.getText().toString());
                                                        profile.setPassword(binding.password.getText().toString());
                                                        profile.setName(binding.name.getText().toString());
                                                        profile.setPhone(binding.etPhn.getText().toString());
                                                        profile.setReg(binding.etReg.getText().toString());
                                                        profile.setLic(binding.etLic.getText().toString());
                                                        profile.setUid(authResult.getUser().getUid());
                                                        // Get the data from an ImageView as bytes
                                                        Bitmap bitmap = profilePhoto;
                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                        byte[] data = baos.toByteArray();

                                                        StorageReference imagesRef = storageRef.child("images/" + binding.email.getText().toString());

                                                        UploadTask uploadTask = imagesRef.putBytes(data);
                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(RegisterActivity.this, "Profile picture could not be uploaded", Toast.LENGTH_SHORT).show();
                                                                profile.setImage(null);
                                                                pd.dismiss();
                                                                Toast.makeText(RegisterActivity.this, "Successfully Account Created", Toast.LENGTH_SHORT).show();
                                                                dbHelper.updateProfile(profile);
                                                                databaseReference.child("users").child(authResult.getUser().getUid()).setValue(profile);
                                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class).addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                                                @SuppressWarnings("VisibleForTests")
                                                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                                profile.setImage(downloadUrl.toString());
                                                                dbHelper.updateProfile(profile);
                                                                databaseReference.child("users").child(authResult.getUser().getUid()).setValue(profile);
                                                                pd.dismiss();
                                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class).addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                                startActivity(intent);
                                                                Toast.makeText(RegisterActivity.this, "Successfully Account Created", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });


                                                        Log.e("AUTH", "Successfully Signed Up");
                                                        Log.e("Auth", authResult.getUser().getEmail());
                                                        Log.e("AUTH", authResult.getUser().getUid());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener(){
                                                    @Override
                                                    public void onFailure(@NonNull Exception e){
                                                        pd.dismiss();
                                                        Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.e("AUTH", e.getMessage());
                                                    }

                                                });
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Please enter your license number", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Please enter your registration number", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }

//                String emailId = emailAddressTextField.getText().toString();
//                String password = passwordTextField.getText().toString();
//                String name = nameTextField.getText().toString();
//                if(TextUtils.isEmpty(emailId)){
//                    Toast.makeText(RegisterActivity.this, "Please enter email address",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(password)){
//                    passwordTextField.setError("Please enter password");
//                    return;
//                }


            }
        });

        binding.ivImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EZPhotoPickConfig config = new EZPhotoPickConfig();
                config.photoSource = PhotoSource.GALLERY; // or PhotoSource.CAMERA
                config.isAllowMultipleSelect = false; // only for GALLERY pick and API >18
                config.exportingSize = 1000;
                EZPhotoPick.startPhotoPickActivity(RegisterActivity.this, config);
            }
        });

        alreadyRegistered.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == EZPhotoPick.PHOTO_PICK_GALLERY_REQUEST_CODE || requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {
            try{
                Bitmap pickedPhoto = new EZPhotoPickStorage(this).loadLatestStoredPhotoBitmap();
                profilePhoto = pickedPhoto;
                binding.ivImage.setImageBitmap(pickedPhoto);
            }catch(IOException e){
                e.printStackTrace();
            }
            //do something with the bitmap
        }
    }

}
