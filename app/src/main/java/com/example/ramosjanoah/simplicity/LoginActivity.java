package com.example.ramosjanoah.simplicity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener { //implements SensorEventListener{


    private Button buttonLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button toGallery;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

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
        finish();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void goImageTest(View view) {
        finish();
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
                            finish();
                            goHome();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
