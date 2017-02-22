package com.example.ramosjanoah.simplicity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by ramosjanoah on 2/18/2017.
 */

public class HomeActivity extends Activity implements OnClickListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String email;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate: HomeActivity.java");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonLogout = (Button) findViewById(R.id.ButtonLogOut);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                    goLogin();
                }
            }
        };

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.HiTextView);
        textViewUserEmail.setText("Hi, " + firebaseAuth.getCurrentUser().getEmail() + "!");

        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
        }
    }
    public void goLogin() {
        Intent toLoginScreen = new Intent(this, LoginActivity.class);
        startActivity(toLoginScreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void editProfile(View view) {
        Intent toLoginScreen = new Intent(this, EditProfile.class);
        startActivity(toLoginScreen);
    }
}














































// ---- Kepentingan Sensor ----

    /*
    TextView healthPoints;
    EditText healthInput;

    private Sensor accelerometer;
    private SensorManager sm;

    private long curTime, lastUpdate;
    private float x, y, z, lastX, lastY, lastZ;
    private final static long UPDATE_PERIOD = 300;
    private final static int SHAKE_THRESHOLD = 800;
    */

// ---------------------------
        /* --Sensor
        healthPoints = (TextView) findViewById(R.id.health);
        healthInput = (EditText) findViewById(R.id.sementara);

        Button switchX = (Button) findViewById(R.id.switchX);
        switchX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int points = Integer.parseInt(healthInput.getText().toString());
                healthPoints.setText(Integer.toString(points));
            }
        });
        this.initialize();
        */// Sensor

// Sensor
        /*
        private void initialize() {
            sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            curTime = lastUpdate = (long)0.0;
            x = y = z = lastX = lastY = lastZ = (float)0.0;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > UPDATE_PERIOD) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //Toast.makeText(this, "Shake detected w/ speed: " +speed,  Toast.LENGTH_SHORT).show();
                    int points = Integer.parseInt(healthPoints.getText().toString());
                    points = points+3;
                    healthPoints.setText(Integer.toString(points));
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // Not Used
        }
        */