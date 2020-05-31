package com.example.betapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PopupPlayersList extends AppCompatActivity {
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
        //try {
            JSONArray players_away_teamJSON = getPlayersByTeamID(getIntent().getStringExtra("away_teamID"));
            if (players_away_teamJSON != null){


            } else {
                TextView msg = new TextView(this);
                msg.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                msg.setText("No Players To Present");
                linearLayout.addView(msg);
            }
            JSONArray players_home_teamJSON = getPlayersByTeamID(getIntent().getStringExtra("home_teamID"));
            if (players_away_teamJSON != null){

            } else {
                TextView msg = new TextView(this);
                msg.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                msg.setText("No Players To Present");
                linearLayout.addView(msg);
            }
        /*} catch (ExecutionException|InterruptedException|JSONException e) {
            e.printStackTrace();
            //TODO: no players to present
        }*/

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
        } else {

        }
    }
    /**
     * TODO:fill
     * @param teamId
     * @return
     */
    public static JSONArray getPlayersByTeamID(String teamId) {
        try {
            return (JSONArray) HttpService.getInstance().
                    getJSON(Consts.PLAYERS_BY_TEAM_ID + teamId).get("player");
        } catch (JSONException|ExecutionException|InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap<String, String> getPlayersByJSON(JSONArray playersJSON) throws JSONException {
        HashMap<String, String> players_id_name = new HashMap<>();
        for(int i=0; i <playersJSON.length(); i++){
            String player_id = (String) ((JSONObject)playersJSON.get(i)).get("idPlayer");
            String player_name = (String) ((JSONObject)playersJSON.get(i)).get("strPlayer");
            players_id_name.put(player_id, player_name);
        }
        return players_id_name;
    }

    private boolean showPlayersInfo(String teamID){
        JSONArray playersJSON = getPlayersByTeamID(teamID);
        ArrayList<CheckBox> players_view = new ArrayList<>();
        try {
            HashMap<String, String> players_map = getPlayersByJSON(playersJSON);
            //TODO: in for loop create Checkbox for each player
            for(Map.Entry<String, String> player_info : players_map.entrySet()){
                CheckBox player_view = new CheckBox(this);
                player_view.setText(player_info.getValue());
                player_view.setTag(player_info.getKey());
                player_view.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                players_view.add(player_view);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }
}
