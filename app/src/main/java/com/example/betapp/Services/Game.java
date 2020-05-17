package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static java.text.DateFormat.getDateInstance;

//TODO: remove game from database
public class Game {
    public String mGroupID;
    public String mDate;
    public String mGameID_API;
    public String mGame_name; //TODO: set name from DB
    public boolean mAvailable_to_bet;
    public HashMap<String, String> mUsers_bets; //user id, bet id
    public HashMap<String, String> mGame_details; //game info
    public boolean isFinished;

    public Game(String groupID, String date, String gameID, HashMap<String, String> users_bets) {
        this.mGroupID = groupID;
        this.mDate = date;
        this.mGameID_API = gameID;
        this.mUsers_bets = users_bets;
        this.mAvailable_to_bet = true;
        this.mGame_details = new HashMap<>();
        String[] keys = {"awayTeamScore", "vff"}; //TODO: add keys as in API to list
        for (String key : keys){
            this.mGame_details.put(key, "");
        }

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

    public static Game getGame(String gameID){
        return null; //TODO: pull game from DB
    }

    public static String uploadToDB(Game game){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry = DB.getReference("games").push().getKey();
        try{
            DB.getReference("games").child(entry).setValue(game);
        } catch(Exception e){
            e.printStackTrace();

        }
        return entry;
    }
}
