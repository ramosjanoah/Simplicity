package com.example.ramosjanoah.simplicity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener { //implements SensorEventListener{


    private Button buttonLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button toGallery;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private SUser UserLogin;
    private String stringEmail;
    public static final String USER_PREFERENCE = "User_Reference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration configInfo = getResources().getConfiguration();
        if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        System.out.println("LoginActivity : OnCreate");
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            goHome();
        }

        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.EmailTextFieldRegister);
        editTextPassword = (EditText) findViewById(R.id.ConfirmPassword);
        buttonLogIn = (Button) findViewById(R.id.GoButton);
        buttonLogIn.setOnClickListener(this);

        toGallery = (Button) findViewById(R.id.toGallery);
        toGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goImageTest(v);
            }
        });

    }

    public void goHome() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void goImageTest(View view) {
        Intent toGalleryScreen = new Intent(this, ImgActivity.class);
        startActivity(toGalleryScreen);
    }

    public void goRegister(View view) {
        finish();
        Intent toRegisterScreen = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterScreen);
    }


    @Override
    public void onClick(View view) {
        if (view == buttonLogIn) {
            userLogIn();
        }
    }

    private void userLogIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // Kalo email kosong?
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            // Kalo password kosong?
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT);
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            GetProfile g = new GetProfile();
                            g.execute();
                            finish();
                            goHome();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                UserLogin = new SUser(firebaseAuth.getCurrentUser().getEmail());
                UserLogin.printUserInformation();
                UserLogin.getUserProfile();
                UserLogin.printUserInformation();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            // Save to SharedPreference
            saveProfile();
        }
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_EMAIL", "Email (Test)");
        outState.putString("USER_FULLNAME", "Fullname (Test)");
        outState.putInt("USER_HEALTH", 0);
        outState.putInt("USER_MUSCLE", 0);
        outState.putString("USER_NATIONALITY", "Nationality (Test)");
        outState.putString("USER_PHOTO", "TBD (Test)");
        saveProfile();
    }*/

    private void saveProfile() {
        SharedPreferences sp = getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        System.out.println("Pair King :");
        UserLogin.printUserInformation();
        spEditor.putString("USER_EMAIL", UserLogin.getEmail());
        spEditor.putString("USER_FULLNAME", UserLogin.getFullname());
        spEditor.putInt("USER_HEALTH", UserLogin.getHealth());
        spEditor.putInt("USER_MUSCLE", UserLogin.getMuscle());
        spEditor.putString("USER_NATIONALITY", UserLogin.getNationality());
        spEditor.putString("USER_PHOTO", UserLogin.getPhoto());
        spEditor.commit();
    }

    @Override
    protected void onStop() {
        //saveProfile();
        super.onStop();
    }
}
