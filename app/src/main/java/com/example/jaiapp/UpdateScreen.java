package com.example.jaiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateScreen extends Activity {
    private EditText editTextUpdatePassword, editTextUpdateFirstName, editTextUpdateLastName, editTextUpdateEmail;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_screen);
        findViews();



        Button buttonUpdating = findViewById(R.id.button_updateInfo);
        buttonUpdating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextUpdateEmail.getText().toString().trim();
                String textPwd = editTextUpdatePassword.getText().toString().trim();
                String textFName = editTextUpdateFirstName.getText().toString();
                String textLName = editTextUpdateLastName.getText().toString();


                // Check to see if data is valid before registering
                if (TextUtils.isEmpty(textFName)) {
                    Toast.makeText(UpdateScreen.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    editTextUpdateFirstName.setError("First name is required!");
                    editTextUpdateFirstName.requestFocus();
                } else if (TextUtils.isEmpty(textLName)) {
                    Toast.makeText(UpdateScreen.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    editTextUpdateLastName.setError("Last name is required!");
                    editTextUpdateLastName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(UpdateScreen.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextUpdateEmail.setError("An email address is required!");
                    editTextUpdateEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd) || textPwd.length() < 6) {
                    Toast.makeText(UpdateScreen.this, "Please enter a valid password (6+ characters)", Toast.LENGTH_SHORT).show();
                    editTextUpdatePassword.setError("A valid password is required!");
                    editTextUpdatePassword.requestFocus();
                } else {
                    updateUser(textEmail, textFName, textLName, textPwd);
                }
            }

            ;


        });


    }

    private void updateUser(String textEmail, String textFName, String textLName, String textPwd) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(UpdateScreen.this , new OnCompleteListener<AuthResult>() {
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
                    Toast.makeText(UpdateScreen.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    writeUpdate(firebaseUser);
                    // Opens the UserProfileActivity after the user has been created
                    Intent userProfileActivity = new Intent(UpdateScreen.this, UserProfile.class);
                    // Stops the user from going back to the register screen
                    userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(userProfileActivity);
                    finish();
                } else {
                    // Handle the exceptions
                    try{
                        throw task.getException();
                    } catch (Exception e){
                        Toast.makeText(UpdateScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void writeUpdate(FirebaseUser newU){
        FirebaseUser user = newU;
        database.child("users").child(""+user.getUid()).setValue(user);
    }

    private void findViews() {
        editTextUpdateEmail = findViewById(R.id.editText_update_email);
        editTextUpdatePassword = findViewById(R.id.editText_update_pad);
        editTextUpdateFirstName = findViewById(R.id.editText_update_fname);
        editTextUpdateLastName = findViewById(R.id.editText_update_lname);
    }
}
