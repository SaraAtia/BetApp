package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;
//TODO: remove bet from database
public class Bet{
    private String mBetID;
    private String mHome_team_score;
    private String mAway_team_score;
    private LinkedList<String> mWho_scored;
    private String mNum_of_yellow_cards;
    private String mNum_of_red_cards;

    public void setAllBet(HashMap<String, String> bets){

    }
    public void setBetID(String m_betID) {
        this.mBetID = m_betID;
    }

    public void setHomeTeamScore(String home_team_score) {
        this.mHome_team_score = mHome_team_score;
    }

    public void setAwayTeamScore(String away_team_score) {
        this.mAway_team_score = away_team_score;
    }

    public void setWhoScored(LinkedList<String> who_scored) {
        this.mWho_scored = who_scored;
    }

    public void setNumOfYellowCards(String num_of_yellow_cards) {
        this.mNum_of_yellow_cards = num_of_yellow_cards;
    }

    public void setNumOfRedCards(String num_of_red_cards) {
        this.mNum_of_red_cards = num_of_red_cards;
    }

    public static String uploadToDB(Bet bet){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry = DB.getReference("bets").push().getKey();
        try{
            DB.getReference("bets").child(entry).setValue(bet);
        } catch(Exception e){
            e.printStackTrace();
        }
        return entry;
    }


}
/*

public class Bet {


    public Bet(String match,int homeTeamScore, int awayTeamScore, LinkedList<String> whoScored, int numOYellowCards,
               int numOfRedCards){
        this.match = match;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.whoScored = whoScored;
        this.numOYellowCards = numOYellowCards;
        this.numOfRedCards = numOfRedCards;
        this.winningTeam = this.WhoWon();
    }

    private int WhoWon(){
        if(this.homeTeamScore>this.awayTeamScore){
            return 1;
        }
        if(this.awayTeamScore>this.homeTeamScore){
            return 2;
        }
        return 0;
    }

    public int getHomeTeamScore(){
        return this.homeTeamScore;
    }
    public int getAwayTeamScore(){
        return this.awayTeamScore;
    }
    public void addScoringPlayer(String playerScoredName){
        this.whoScored.add(playerScoredName);
    }

    public int getNumOYellowCards(){
        return this.numOYellowCards;
    }
    public int getNumOfRedCards(){
        return this.numOfRedCards;
    }

    public LinkedList<String> getWhoScored() {
        return whoScored;
    }

    public String scoringPlayersToString(){
        String scoring="";
        int i;
        for (i = 0; i < this.whoScored.size() - 1; i++){
            scoring += this.whoScored.get(i) + ", ";
        }
        scoring += this.whoScored.get(i);
        return scoring;
    }


    public String getMatch() {
        return match;
    }

    public int getWinningTeam(){
        return this.winningTeam;
    }

}
*/
