package com.example.jaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterEmail, editTextRegisterPwd, editTextRegisterFirstName, editTextRegisterLastName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        getSupportActionBar().setTitle("Sign up!");
        database = FirebaseDatabase.getInstance().getReference();
        findViews();
        showHidePassword();


        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textFName = editTextRegisterFirstName.getText().toString();
                String textLName = editTextRegisterLastName.getText().toString();


                // Check to see if data is valid before registering
                if (TextUtils.isEmpty(textFName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFirstName.setError("First name is required!");
                    editTextRegisterFirstName.requestFocus();
                } else if (TextUtils.isEmpty(textLName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    editTextRegisterLastName.setError("Last name is required!");
                    editTextRegisterLastName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("An email address is required!");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd) || textPwd.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Please enter a valid password (6+ characters)", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("A valid password is required!");
                    editTextRegisterPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textEmail, textFName, textLName, textPwd);
            }
        };


    });


}

    private void registerUser(String textEmail, String textFName, String textLName, String textPwd) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Check to see if the user was completely successfully
                FirebaseUser firebaseUser = null;
                if(task.isSuccessful()){
                    firebaseUser = auth.getCurrentUser();
                }
                if (firebaseUser != null){
                    // Update display name of the user
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFName + " " + textLName).build();
                    firebaseUser.updateProfile(profileUpdates);
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    // Opens the UserProfileActivity after the user has been created
                    Intent userProfileActivity = new Intent(RegisterActivity.this, UserProfile.class);
                    // Stops the user from going back to the register screen
                    userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(userProfileActivity);
                    finish();
                } else {
                    // Handle the exceptions
                    try{
                        throw task.getException();
                    } catch (Exception e){
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void writeNewUser(String textEmail, String textFname){
        User user = new User(textFname, textEmail);
        database.child("users").child(""+user.getID()).setValue(user);
    }

    private void findViews() {
        View editTextRegisterEmail = findViewById(R.id.editText_register_email);
        View editTextRegisterPwd = findViewById(R.id.editText_register_pad);
        View editTextRegisterFirstName = findViewById(R.id.editText_register_fname);
        editTextRegisterLastName = findViewById(R.id.editText_register_lname);
        progressBar = findViewById(R.id.progressbar);
    }

    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.ImageView_show_hide_pad);
        imageViewShowHidePwd.setImageResource(R.drawable.visibility);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibilityOff);
                } else {
                    editTextRegisterPwd.setTransformationMethod((HideReturnsTransformationMethod.getInstance()));
                    imageViewShowHidePwd.setImageResource(R.drawable.visibility);
                }
    }

});
}

}