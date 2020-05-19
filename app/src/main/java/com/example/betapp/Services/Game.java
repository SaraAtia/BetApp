package com.example.betapp.Services;

import com.example.betapp.Consts;
import com.example.betapp.Services.HttpService.HttpService;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


//TODO: remove game from database
public class Game {
    public String mGroupID;
    public String mGameID;
    public String mDate;
    public String mGameID_API;
    public String mGame_name;
    public boolean mAvailable_to_bet;
    private HashMap<String, String> mUsers_bets; //user id, bet id
    private HashMap<String, String> mGame_details; //game info
    private boolean isFinished;

    public Game(){}
    public Game(String groupID, String gameID, String date, String gameID_API, HashMap<String, String> users_bets) {
        this.mGroupID = groupID;
        this.mGameID = gameID;
        this.mDate = date;
        this.mGameID_API = gameID_API;
        this.mUsers_bets = users_bets;
        this.mAvailable_to_bet = true;
        this.mGame_details = new HashMap<>();
    }

    public Game(String groupID, String date, String gameID, String gameName) {
        this.mGroupID = groupID;
        this.mDate = date;
        this.mGameID_API = gameID;
        this.mGame_name = gameName;
        this.mUsers_bets = new HashMap<>();
        this.mAvailable_to_bet = true;
        this.mGame_details = new HashMap<>();
    }

    public void setMapValues(ArrayList<String> keys, ArrayList<String> values){
        int length = keys.size();
        for(int i = 0; i < length; i++){
            this.mGame_details.put(keys.get(i), values.get(i));
        }
    }

    public static HashMap<String, String> getGameDetails(String gameID_API){
        HashMap<String, String> game_details = new HashMap<>();
        try {
            JSONArray events_array =(JSONArray) HttpService.getInstance().
                    getJSON(Consts.GAME_DETAILS_BY_EVENT_ID+gameID_API).get("events");
            JSONObject game_info = (JSONObject) events_array.get(0);
            game_details.put("idEvent",game_info.getString("idEvent"));
            game_details.put("strEvent",game_info.getString("strEvent"));
            game_details.put("idLeague",game_info.getString("idLeague"));
            game_details.put("strLeague",game_info.getString("strLeague"));
            game_details.put("strHomeTeam",game_info.getString("strHomeTeam"));
            game_details.put("strAwayTeam",game_info.getString("strAwayTeam"));
            game_details.put("dateEvent",game_info.getString("dateEvent"));
            game_details.put("strTimeLocal",game_info.getString("strTimeLocal"));
            return game_details;
        } catch (InterruptedException | ExecutionException| JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadToDB(Game game){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry = DB.getReference("games").push().getKey();
        game.mGameID = entry;
        DB.getReference("games").child(entry).setValue(game);
        return entry;
    }

    public void setGameID(String game_entry) {
        this.mGameID = game_entry;
    }
}
