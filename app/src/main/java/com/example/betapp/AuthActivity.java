package com.example.betapp;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.example.betapp.Services.AuthService.AuthService;
import android.view.inputmethod.InputMethodManager;

public class AuthActivity extends AppCompatActivity {
    AuthService authService;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        this.authService = AuthService.getInstance();
        rememberMe = findViewById(R.id.rememberMe);
//        this.startService(); // TODO: uncomment to start notification service

        // check if 'remember me' checkbox was checks
        SharedPreferences prefs = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = prefs.getString("remember","");
        if (checkbox.equals("true")){
            openMyLeaguesActivity();
        }

        // when checkbox is checked
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("remember","true");
                    editor.apply();
                } else if (!buttonView.isChecked()){
                    SharedPreferences prefs = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.authService.setIsLoggedIn(false);
    }

    public void signIn(View view){
        //TODO: delete
        //########################################################
        try {
            authService.signInOrSignUp("sara@betapp.com",
                    "sara1234", AuthService.SIGN_IN);
            openMyLeaguesActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //###############################################################
        // get fields
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);

        // get login information
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        try {
            authService.signInOrSignUp(email_address, password, AuthService.SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (authService.getIsLoggedIn()) {
            openMyLeaguesActivity();

        } else {
            popupMessage("Credentials incorrect");
        }
    }

    public void signUp(View view){
        // get fields
        EditText email_address_field = findViewById(R.id.email_address);
        EditText password_field = findViewById(R.id.password);

        // get login information
        String email_address = email_address_field.getText().toString();
        String password = password_field.getText().toString();

        int result = 0;
        try {
            result = authService.signInOrSignUp(email_address, password, AuthService.SIGN_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == 1) {
            popupMessage("Signed up successfully");

        } else {
            popupMessage("Could not sign up");
        }

    }

    public void openMyLeaguesActivity() {
        Intent intent = new Intent(this, MyLeagues.class);
        startActivity(intent);
    }

    private void hideKeyboard(View view){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            view = this.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

}
