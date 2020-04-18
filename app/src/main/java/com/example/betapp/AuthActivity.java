package com.example.betapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.inputmethod.InputMethodManager;

public class AuthActivity extends AppCompatActivity {
    CheckBox rememberMe;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
//        this.startService(); // TODO: uncomment to start notification service
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
        openMyLeaguesActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        this.authService.setIsLoggedIn(false);
    }

    public void signIn(View view){
        // get fields
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);

        // get login information
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        signIn(email_address, password);
    }

    public void signUp(View view){
        // get fields
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);

        // get login information
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        createAccount(email_address,password);

    }

    public void openMyLeaguesActivity() {
        Intent intent = new Intent(this, MyLeagues.class);
        startActivity(intent);
    }

    private void popupMessage(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void startService(){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            openMyLeaguesActivity();
                        } else {
                            popupMessage("Credentials incorrect");
                        }
                    }
                });
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            openMyLeaguesActivity();
                        } else {
                            popupMessage("Can't create account");
                        }
                    }
                });
    }

}
