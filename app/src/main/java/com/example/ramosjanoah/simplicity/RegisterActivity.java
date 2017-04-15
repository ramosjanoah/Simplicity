package com.example.ramosjanoah.simplicity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ramosjanoah on 2/18/2017.
 */
public class RegisterActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextNationality;


    private TextView AlreadyRegister;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private SUser UserToRegister;

    private GoogleApiClient mGoogleApiClient;
    private String PasswordForAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Configuration configInfo = getResources().getConfiguration();
        if (configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.RegisterButtonRegister);
        editTextEmail = (EditText) findViewById(R.id.EmailTextFieldRegister);
        editTextPassword = (EditText) findViewById(R.id.PasswordTextField);
        editTextConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        editTextNationality = (EditText) findViewById(R.id.Nationality);
        AlreadyRegister = (TextView) findViewById(R.id.AlreadyRegister);

        buttonRegister.setOnClickListener(this);
        AlreadyRegister.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    public void goLogin() {
        Intent toLoginScreen = new Intent(this, LoginActivity.class);
        startActivity(toLoginScreen);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        } else if (view == AlreadyRegister) {
            finish();
            goLogin();
        }
    }

    public void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        System.out.println("xxxxx1");
        System.out.println(email + ", " + password + ", " + confirmPassword);

        if (TextUtils.isEmpty(email)) {
            // Kalo email kosong?
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT);
            return;
        } else if (TextUtils.isEmpty(password)) {
            // Kalo password kosong?
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT);
            return;
        } else if (!(confirmPassword.equals(password))) {
            // Kalo confirm password gagal
            Toast.makeText(this, "Please confirm your password correcty", Toast.LENGTH_SHORT);
            return;
        } else {
            System.out.println("xxxxx2");

            progressDialog.setMessage("Registering User...");
            progressDialog.show();
            PasswordForAPI = password;
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Kalau register sukses
                            if (task.isSuccessful()) {
                                UserToRegister = new SUser(email,
                                        editTextNationality.getText().toString());
                                // Register!
                                RegisterProfile register = new RegisterProfile();
                                register.execute();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }

                    });
        }
    }

    public class RegisterProfile extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                UserToRegister.writeUser(PasswordForAPI);
//                UserToRegister.writeUserToAPI(PasswordForAPI);
//                UserLogin = new SUser(firebaseAuth.getCurrentUser().getEmail());
//                UserLogin.printUserInformation();
//                UserLogin.getUserProfile();
//                UserLogin.printUserInformation();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Register success.",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            // Save to SharedPreference

        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("On Connected");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            System.out.println(latitude);
            System.out.println(longitude);


            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses != null) {
                    if (addresses.size() > 0) {
                        String cityName = addresses.get(0).getCountryName();
                        editTextNationality.setText(cityName);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error get location");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("Connection Failed");
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        System.out.println("Connection Suspended");
        mGoogleApiClient.connect();
    }

}
