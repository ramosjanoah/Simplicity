package com.example.ramosjanoah.simplicity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by ramosjanoah on 2/18/2017.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private TextView AlreadyRegister;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.RegisterButtonRegister);
        editTextEmail = (EditText) findViewById(R.id.EmailTextFieldRegister);
        editTextPassword = (EditText) findViewById(R.id.PasswordTextField);
        editTextConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        AlreadyRegister = (TextView) findViewById(R.id.AlreadyRegister);

        buttonRegister.setOnClickListener(this);
        AlreadyRegister.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
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
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        System.out.println("xxxxx1");
        System.out.println(email + ", " + password + ", " + confirmPassword );

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

        if (!(confirmPassword.equals(password))) {
            // Kalo confirm password gagal
            Toast.makeText(this, "Please confirm your password correcty", Toast.LENGTH_SHORT);
            return;
        }

        System.out.println("xxxxx2");

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Kalau register sukses
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register success. Log in to continue.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();
                    }

                });
    }
    /*
    public class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Loading user profile..");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.hide();
            printUserProfile();
        }*/
}
