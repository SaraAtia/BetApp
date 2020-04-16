package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;


public class CreateGroup extends AppCompatActivity {
    final static HashMap<String, String> btnMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        addLeaguesBtns();
    }
    public void addLeaguesBtns(){
        LinearLayout l = findViewById(R.id.choose_leagues_layout);
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader
                    (getAssets().open(Consts.LEAGUES_FILE_NAME)));
            String line = buffer.readLine();
            String[] temp;
            String key, val;
            while (line != null) {
                temp = line.split(":");
                val = temp[0];
                key = temp[1];
                btnMap.put(key, val);
                final Button btnShow = new Button(this);
                btnShow.setId(Integer.parseInt(key));
                btnShow.setText(val);
                btnShow.setPadding(1, 1, 1, 1);
                btnShow.setTextSize(15);
                btnShow.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLeaguesGame(btnMap, btnShow);
                    }
                });
                l.addView(btnShow);
                line = buffer.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void openLeaguesGame(HashMap<String, String> btnMap, Button btnShow){
        Intent intent = new Intent(this, ChooseLeagueGames.class);
        intent.putExtra("leagueID", String.valueOf(btnShow.getId()));
        startActivity(intent);
    }
}
