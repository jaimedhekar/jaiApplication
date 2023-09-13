package com.example.jaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class UserProfile extends AppCompatActivity {

    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewEmail, textViewRegisterDate;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");
        findViews();
        signOut();


        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null){
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }else{
            Toast.makeText(UserProfile.this, "User Not Found", Toast.LENGTH_SHORT);
        }

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        FirebaseUserMetadata metadata = firebaseUser.getMetadata();
        long registerTimeStamp = metadata.getCreationTimestamp();
        String datePattern = "E_ dd MMMM yyyy hh:mm a z";
        SimpleDateFormat sdf= new SimpleDateFormat(datePattern);
        sdf.setTimeZone(TimeZone.getDefault());
        String register = sdf.format(new Date(registerTimeStamp));
        String registerDate = getResources().getString(R.string.user_since, register);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        textViewEmail.setText(email);
        textViewFirstName.setText(name);
    }

    private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                auth.signOut();
                Toast.makeText(UserProfile.this, "Signed Out", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(UserProfile.this, MainActivity.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);

            }
        });
    }

    private void findViews() {
        textViewWelcome = findViewById(R.id.textView_welcome);
        textViewFirstName = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewRegisterDate = findViewById(R.id.textView_register);
        progressBar = findViewById(R.id.progressbar);


    }
}