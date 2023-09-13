package com.example.jaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPad;
    private ProgressBar progressBar;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("login");
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPad = findViewById(R.id.editText_login_pad);
        progressBar = findViewById(R.id.progressbar);

        showHidePassword();

        TextView textViewRegister = findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        auth = FirebaseAuth.getInstance();
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPad.getText().toString();
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email address is required!");
                    editTextLoginEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(MainActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    editTextLoginPad.setError("Password is required!");
                    editTextLoginPad.requestFocus();
                }else if(textPassword.length() < 6){
                    Toast.makeText(MainActivity.this, "Please enter Password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextLoginPad.setError("Password is required to be more than 6 characters!");
                    editTextLoginPad.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPassword);
                }
            }
        });

    }

    private void loginUser(String textEmail, String textPassword) {
        auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                
            }
        });
    }

    private void showHidePassword() {
        ImageView imageViewShowHidePad = findViewById(R.id.ImageView_show_hide_pad);
        imageViewShowHidePad.setImageResource(R.drawable.visibility2);
        imageViewShowHidePad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLoginPad.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextLoginPad.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePad.setImageResource(R.drawable.visibility2);
                } else {
                    editTextLoginPad.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePad.setImageResource(R.drawable.visibilityOff);
                }
            }

        })
                ;
    }


}