package com.example.ramosjanoah.simplicity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Personal on 2/22/2017.
 */

public class ImgActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button galleryBtn;
    private ImageView imageView;
    private TextView locTxt;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Img Test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_test);

        locTxt = (TextView) findViewById(R.id.testLocation);


// Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        String locationProvider = LocationManager.NETWORK_PROVIDER;
//        Location location = locationManager.getLastKnownLocation(locationProvider);
//
//        //GeoCoder
//        double lat = location.getLatitude();
//        double lng = location.getLongitude();
//
//        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
//        StringBuilder builder = new StringBuilder();
//        try {
//            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
//            int maxLines = address.get(0).getMaxAddressLineIndex();
//            for (int i = 0; i < maxLines; i++) {
//                String addressStr = address.get(0).getAddressLine(i);
//                builder.append(addressStr);
//                builder.append(" ");
//            }
//            String fnialAddress = builder.toString(); //This is the complete address.
//            locTxt.setText(fnialAddress); //This will display the final address.
//        } catch (IOException e) {
//            // Handle IOException
//        } catch (NullPointerException e) {
//            // Handle NullPointerException
//        }
//        //End Geocoder
        galleryBtn = (Button) findViewById(R.id.testGallery);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 12);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        finish();
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
                        locTxt.setText(cityName);
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
