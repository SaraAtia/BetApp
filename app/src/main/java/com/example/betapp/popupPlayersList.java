package com.example.betapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class popupPlayersList extends AppCompatActivity {
    LinearLayout linearLayout;
    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<CheckBox> checkBoxes_arr = new ArrayList<>();
    static ArrayList<Integer> d = new ArrayList<>();
    CheckBox c1, c2, c3, c4, c5;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayout = (LinearLayout)findViewById(R.layout.activity_popup_players_list);
        setContentView(R.layout.activity_popup_players_list);
        try {
            JSONObject players_away_teamJSON = getPlayersByTeamID(getIntent().getStringExtra("away_teamID"));
            if (players_away_teamJSON == null){
                TextView msg = new TextView(this);
                msg.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                msg.setText("No Players To Present");
                linearLayout.addView(msg);
            }
            JSONObject players_home_teamJSON = getPlayersByTeamID(getIntent().getStringExtra("home_teamID"));
            if (players_away_teamJSON == null){
                TextView msg = new TextView(this);
                msg.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                msg.setText("No Players To Present");
                linearLayout.addView(msg);
            }
        } catch (ExecutionException|InterruptedException|JSONException e) {
            e.printStackTrace();
            //TODO: no players to present
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // TODO: get players names - create checkbox for each.
        getWindow().setLayout((int)(width*.8),(int)(height*.75));
        if(!checkBoxes_arr.isEmpty()){
            for(CheckBox c: checkBoxes_arr){
                c.setChecked(true);
            }
        }
        else {
        }



    }

/*
    public void addToArrays(CheckBox c){
        players.add(c.getText().toString());
        checkBoxes_arr.add(c);
        d.add(1);
    }

    public void removeFromArrays(CheckBox c){
        players.remove(c.getText().toString());
        checkBoxes_arr.remove(c);
    }

    public void checkFunc(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox1:
                if (checked)
                    addToArrays(c1);
                else
                    removeFromArrays(c1);
                break;
            case R.id.checkbox2:
                if (checked)
                    addToArrays(c2);
                else
                    removeFromArrays(c2);
                break;
            case R.id.checkbox3:
                if (checked)
                    addToArrays(c3);
                else
                    removeFromArrays(c3);
                break;
            case R.id.checkbox4:
                if (checked)
                    addToArrays(c4);
                else
                    removeFromArrays(c4);
                break;
        }
    }*/

    public JSONObject getPlayersByTeamID(String teamId)throws ExecutionException,
            InterruptedException, JSONException {
        return (JSONObject) HttpService.getInstance().
                getJSON(Consts.PLAYERS_BY_TEAM_ID + teamId).get("player");
    }
}
