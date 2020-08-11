package com.example.betapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.betapp.Services.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AuthActivity extends AppCompatActivity {
    CheckBox rememberMe;
    private FirebaseAuth mAuth;
    public static User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
                openMyGroupsActivity(null, currentUser.getUid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        this.authService.setIsLoggedIn(false);
    }

    public void signIn(View view){
        // get fields
        EditText user_name_field = findViewById(R.id.user_name);
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);


        // get login information
        String user_name = user_name_field.getText().toString();
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        signIn(user_name, email_address, password);
    }

    public void signUp(View view){
        // get fields
        EditText user_name_field = findViewById(R.id.user_name);
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);

        // get login information
        String user_name = user_name_field.getText().toString();
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        createAccount(user_name, email_address,password);

    }

    public void openMyGroupsActivity(String user_name, String userID) {
        Intent intent = new Intent(this, MyGroups.class);
        intent.putExtra("userIDAuth", userID);
        intent.putExtra("user_name", user_name);
        startActivity(intent);
    }

    private void popupMessage(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void signIn(final String user_name, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            openMyGroupsActivity(user_name, user.getUid());
                        } else {
                            popupMessage("Credentials incorrect");
                        }
                    }
                });
    }

    private void createAccount(final String user_name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            openMyGroupsActivity(user_name, user.getUid());
                        } else {
                            popupMessage("Can't create account");
                        }
                    }
                });
    }
}
