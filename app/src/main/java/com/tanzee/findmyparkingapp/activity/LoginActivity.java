package com.tanzee.findmyparkingapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import corp.tz.findmyparking.R;

public class LoginActivity extends Activity {

    private Button navToRegScreen;


    private EditText mEmail;
    private EditText mPassword;

    private Button mLoginButton;
    private FirebaseAuth mAuth;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navToRegScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.btnLogin);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please Wait");

        mAuth = FirebaseAuth.getInstance();

        navToRegScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mEmail.getText().toString().equals("") || mPassword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email And Password", Toast.LENGTH_SHORT).show();
                }else{
                    logIn();
                }

            }
        });
    }


    /**
     * Initializes Facebook Account Kit Sms flow registration.
     */
    private void logIn(){

        pd.show();

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                pd.dismiss();
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class).addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    startActivity(intent);
                }
            }
        });


    }
}

