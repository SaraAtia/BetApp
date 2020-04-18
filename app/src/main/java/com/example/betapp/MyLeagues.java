package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyLeagues extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_my_leagues);
        LinearLayout l = findViewById(R.id.my_leagues_layout);
        ArrayList<String> btnStr = new ArrayList<>();
        btnStr.add("first");
        btnStr.add("second");
        btnStr.add("third");
        btnStr.add("fourth");
        btnStr.add("fifth");
        btnStr.add("sixth");
        btnStr.add("seventh");

        for(int i =0; i <7; i++){
            Button btnShow = new Button(this);
            btnShow.setText(btnStr.get(i));
            btnShow.setLayoutParams(new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Works!");
                }
            });

            l.addView(btnShow);
        }


    }

    public void createLeague(View view){
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }
    public void joinLeague(View view) {
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }

    public void signOut(View view){
        mAuth.signOut();
        startActivity(new Intent(this, AuthActivity.class));
    }


}
