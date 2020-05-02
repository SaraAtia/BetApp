package com.example.betapp.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static java.text.DateFormat.getDateInstance;

//TODO: remove game from database
public class Game {
    String m_groupID;
    Date m_date;
    String m_gameID;
 	boolean m_available_to_bet;
 	HashMap<String, String> m_users_bets; //user id, bet id
 	HashMap<String, String> m_game_details; //game info
    private boolean isFinished;

    public Game(String groupID, String date, String gameID, HashMap<String, String> users_bets) {
        this.m_groupID = groupID;
        try{
            this.m_date = getDateInstance().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.m_gameID = gameID;
        this.m_users_bets = users_bets;
        this.m_available_to_bet = true;
        this.m_game_details = new HashMap<>();
        String[] keys = {"awayTeamScore", "vff"}; //TODO: add keys as in API to list
        for (String key : keys){
            this.m_game_details.put(key, "");
        }

    }
    public Game(String groupID, String date, String gameID) {
        this.m_groupID = groupID;
        try{
            this.m_date = getDateInstance().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.m_gameID = gameID;
        this.m_users_bets = new HashMap<>();
        this.m_available_to_bet = true;
        this.m_game_details = new HashMap<>();
    }

    public void setMapValues(ArrayList<String> keys, ArrayList<String> values){
        int length = keys.size();
        for(int i = 0; i < length; i++){
            this.m_game_details.put(keys.get(i), values.get(i));
        }
    }

    public static Game getGame(String gameID){
        return null; //TODO: pull game from DB
    }
}
