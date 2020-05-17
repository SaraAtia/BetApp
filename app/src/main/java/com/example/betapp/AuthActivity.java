package com.example.betapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
            System.out.println(currentUser.getUid());
            openMyGroupsActivity(currentUser.getUid());
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

    public void openMyGroupsActivity(String userID) {
        Intent intent = new Intent(this, MyGroups.class);
        intent.putExtra("userIDAuth", userID);
        startActivity(intent);
    }

    private void popupMessage(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void signIn(String email, String password){
        /////////////////////// TODO: delete from here //////////////////////////
       /* if(email.isEmpty()){
            email = "sara@betapp.com";
            password = "123456";
        }*/
        /////********************* Till here *************************//////////////////
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println(user.getUid());
                            openMyGroupsActivity(user.getUid());
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
                            System.out.println(user.getUid());
                            openMyGroupsActivity(user.getUid());
                        } else {
                            popupMessage("Can't create account");
                        }
                    }
                });
    }
}
