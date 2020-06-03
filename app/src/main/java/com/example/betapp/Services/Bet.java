package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//TODO: remove bet from database
public class Bet {
    private String mBetID;
    public String mHome_team_score;
    public String mAway_team_score;
    public HashMap<String, String> mAway_team_players_scored;// id: name
    public HashMap<String, String> mHome_team_players_scored;
    public String mNum_of_yellow_cards;
    public String mNum_of_red_cards;

    public Bet(String homeTeamScore, String awayTeamScore, String numOYellowCards,
               String numOfRedCards,  HashMap<String, String> home_team_players_scored, HashMap<String, String> away_team_players_scored) {
        this.mHome_team_score = homeTeamScore;
        this.mAway_team_score = awayTeamScore;
        this.mNum_of_yellow_cards = numOYellowCards;
        this.mNum_of_red_cards = numOfRedCards;
        this.mHome_team_players_scored = home_team_players_scored;
        this.mAway_team_players_scored = away_team_players_scored;
    }

    public Bet(JSONObject betJSON){
        try {
            mBetID = betJSON.getString("mBetID");
            mHome_team_score = betJSON.getString("mHome_team_score");
            mAway_team_score = betJSON.getString("mAway_team_score");
            mNum_of_yellow_cards = betJSON.getString("mNum_of_yellow_cards");
            mNum_of_red_cards = betJSON.getString("mNum_of_red_cards");
            mHome_team_players_scored = new HashMap<>();
            mAway_team_players_scored = new HashMap<>();
            JSONObject away_players = betJSON.getJSONObject("mAway_team_players_scored");
            JSONObject home_players = betJSON.getJSONObject("mHome_team_players_scored");
            for (Iterator<String> it = away_players.keys(); it.hasNext(); ) {
                String player_id = it.next();
                String player_name = away_players.getString(player_id);
                mAway_team_players_scored.put(player_id, player_name);
            }
            for (Iterator<String> it = home_players.keys(); it.hasNext(); ) {
                String player_id = it.next();
                String player_name = home_players.getString(player_id);
                mHome_team_players_scored.put(player_id, player_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //return null;
        }


    }

    public String getmBetID() {
        return mBetID;
    }

    public static String uploadToDB(Bet bet) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry = DB.getReference("bets").push().getKey();
        bet.mBetID = entry;
        DB.getReference("bets").child(entry).setValue(bet);
        return entry;
    }
}