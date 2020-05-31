package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//TODO: remove bet from database
public class Bet {
    private String mBetID;
    public String mHome_team_score;
    public String mAway_team_score;
    public HashMap<String, HashMap<String, String>> mPlayers_scored_by_teamID; //id : name
    public String mNum_of_yellow_cards;
    public String mNum_of_red_cards;

    public Bet(String homeTeamScore, String awayTeamScore, String numOYellowCards,
               String numOfRedCards, HashMap<String, HashMap<String, String>> players_scored) {
        this.mHome_team_score = homeTeamScore;
        this.mAway_team_score = awayTeamScore;
        this.mNum_of_yellow_cards = numOYellowCards;
        this.mNum_of_red_cards = numOfRedCards;
        this.mPlayers_scored_by_teamID = players_scored;
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