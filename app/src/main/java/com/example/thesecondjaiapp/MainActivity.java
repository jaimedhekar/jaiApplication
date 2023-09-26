package com.example.thesecondjaiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.jaiapp.R;
import com.example.jaiapp.RegisterActivity;
import com.example.jaiapp.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLogEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        editTextLogEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pad);
        progressBar = findViewById(R.id.progressbar);

        showHidePassword();


        TextView textViewRegister = findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        auth = FirebaseAuth.getInstance();
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLogEmail.getText().toString();
                String textPassword = editTextLoginPwd.getText().toString();
                //verify info
                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(MainActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextLogEmail.setError("An email address is required!");
                    editTextLogEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword) || textPassword.length() < 6){
                    Toast.makeText(MainActivity.this, "Please enter a valid password (6+ characters)", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("A valid password is required!");
                    editTextLoginPwd.requestFocus();
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
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent userProfileActivity = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(userProfileActivity);
                }else{
                    try{
                        throw task.getException();
                    }catch(Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.ImageView_show_hide_pad);
        imageViewShowHidePwd.setImageResource(R.drawable.visibilityoff);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibilityOff);
                } else {
                    editTextLoginPwd.setTransformationMethod((HideReturnsTransformationMethod.getInstance()));
                    imageViewShowHidePwd.setImageResource(R.drawable.visibilityoff);
                }
            }
        });
    }
}




