package com.example.ramosjanoah.simplicity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class EditProfile extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextNationality;

    private Button buttonSaveProfile;

    private SUser CurrentUser;
    private SharedPreferences sp;
    private static String USER_PREFERENCE = "User_Reference";
    private SUser userToUpdate;

    private String newestFullname;
    private String newestNationality;
    private int lastHealth;
    private int lastMuscle;
    private String lastEmail;
    private String lastPhoto;
    private ProgressDialog progressDialog;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Configuration configInfo = getResources().getConfiguration();
        if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        editTextName = (EditText) findViewById(R.id.editName);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextNationality = (EditText) findViewById(R.id.editNationality);
        progressDialog = new ProgressDialog(this);

        sp = getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (!sp.getString("USER_EMAIL", "EMPTY").equals("EMPTY")) {
            editTextEmail.setText(sp.getString("USER_EMAIL", null));
            editTextName.setText(sp.getString("USER_FULLNAME", null));
            editTextNationality.setText(sp.getString("USER_NATIONALITY", null));
        } else {
            editTextEmail.setText("Email Not Found");
            editTextName.setText("Fullname Not Found");
            editTextNationality.setText("Nationality Not Found");
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        buttonSaveProfile = (Button) findViewById(R.id.buttonSaveEdit);
        buttonSaveProfile.setOnClickListener(this);
    }

    public void saveEdit(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        if (view == buttonSaveProfile) {
            // inisialisasi
            newestFullname = editTextName.getText().toString();
            newestNationality = editTextNationality.getText().toString();
            lastEmail = editTextEmail.getText().toString();
            lastHealth = sp.getInt("USER_HEALTH", SUser.DEFAULT_HEALTH);
            lastHealth = sp.getInt("USER_MUSCLE", SUser.DEFAULT_MUSCLE);
            lastPhoto = sp.getString("USER_PHOTO", SUser.DEFAULT_PHOTO);
            userToUpdate = new SUser(lastEmail, newestFullname, newestNationality, lastHealth, lastMuscle);
            userToUpdate.setPhoto(lastPhoto);
            // panggil asynctask
            progressDialog.setMessage("Updating profile...");
            progressDialog.show();

            UpdateProfile u = new UpdateProfile();
            u.execute();
        }
    }

    public class UpdateProfile extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                userToUpdate.updateUser();
                sp = getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString("USER_EMAIL", lastEmail);
                spEditor.putString("USER_FULLNAME", newestFullname);
                spEditor.putInt("USER_HEALTH", userToUpdate.getHealth());
                spEditor.putInt("USER_MUSCLE", userToUpdate.getMuscle());
                spEditor.putString("USER_NATIONALITY", userToUpdate.getNationality());
                spEditor.putString("USER_PHOTO", userToUpdate.getPhoto());
                spEditor.commit();
                System.out.println(sp.getString("USER_EMAIL", "NOT FOUND"));
                System.out.println(sp.getString("USER_FULLNAME", "NOT FOUND"));

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
        }

        @Override
        protected void onPostExecute(String s) {
            //
            progressDialog.dismiss();
            // Save to SharedPreference

            // Finish the Edit Profile
            finish();
            // On Create Home lagi
            goHome();
        }

        public void goHome() {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
