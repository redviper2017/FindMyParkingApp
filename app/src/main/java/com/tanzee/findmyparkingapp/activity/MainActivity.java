package com.tanzee.findmyparkingapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpURLConnection;

import corp.tz.findmyparking.R;
import corp.tz.findmyparking.dto.SignupRequestDto;
import corp.tz.findmyparking.dto.SignupResponseDto;
import corp.tz.findmyparking.service.ApiService;
import corp.tz.findmyparking.utils.Constant;
import corp.tz.findmyparking.utils.SPreference;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private Button log;
    private Button reg;
    private Button out;
    private FirebaseAuth firebaseAuth;
    private SPreference sPreference;
    private ApiService apiService;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        log = (Button) findViewById(R.id.btn_signIn);
        reg = (Button) findViewById(R.id.btn_signUp);
        out = (Button) findViewById(R.id.btn_signOut);
        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait");
        pd.setCancelable(false);

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        apiService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        sPreference = new SPreference(this);

        log.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //firebaseAuth.signOut();
                finish();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * Initializes Facebook Account Kit Sms flow registration.
     */
//    private void logIn() {
//        final Intent intent = new Intent(this, AccountKitActivity.class);
//        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
//                new AccountKitConfiguration.AccountKitConfigurationBuilder(
//                        LoginType.PHONE,
//                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
//        // ... perform additional configuration ...
//        intent.putExtra(
//                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
//                configurationBuilder.build());
//        startActivityForResult(intent, Constant.FB_ACCOUNTLIT_ACTIVITY_RESULT_CODE);
//
//    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == Constant.FB_ACCOUNTLIT_ACTIVITY_RESULT_CODE) { // confirm that this response matches your request
//            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
//            String toastMessage;
//            if (loginResult.getError() != null) {
//                toastMessage = loginResult.getError().getErrorType().getMessage();
//                //showErrorActivity(loginResult.getError());
//            } else if (loginResult.wasCancelled()) {
//                toastMessage = "Login Cancelled";
//            } else {
//                if (loginResult.getAccessToken() != null) {
//                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
//                    //loginResult.getAccessToken().
//                } else {
//                    toastMessage = String.format(
//                            "Success:%s...",
//                            loginResult.getAuthorizationCode().substring(0, 10));
//                }
//            }
//
//            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                @Override
//                public void onSuccess(Account account) {
//
//                    PhoneNumber phoneNumber = account.getPhoneNumber();
//                    String phoneNumberString = phoneNumber.toString();
//                    //sPreferences.setPhoneNumber(phoneNumberString);
//                    Log.e("number", "asdf" + phoneNumberString);
//                    makeSignUp(phoneNumberString);
//                }
//
//                @Override
//                public void onError(AccountKitError accountKitError) {
//                    Log.e("number", "asdf" + accountKitError.getUserFacingMessage());
//                }
//            });
//            //Log.e("number", "asdf" + AccountKit.getCurrentPhoneNumberLogInModel().getPhoneNumber());
//        }


    }

    private void makeSignUp(final String phoneNumberString){
        pd.show();
        SignupRequestDto dto = new SignupRequestDto();
        dto.setPhoneNumber(phoneNumberString);
        apiService.signUp(dto).enqueue(new Callback<SignupResponseDto>(){
            @Override
            public void onResponse(Call<SignupResponseDto> call, Response<SignupResponseDto> response){
                pd.dismiss();
                if(response.code() == HttpURLConnection.HTTP_OK){
                    sPreference.setPhoneNumber(phoneNumberString);
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }else {
                    Toast.makeText(MainActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponseDto> call, Throwable t){
                pd.dismiss();
                Log.e("err", t.getMessage());
                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(!sPreference.getPhoneNumber().equals("na")){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }else {

        }
    }
}
