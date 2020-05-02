package com.example.betapp.Services;

import java.util.HashMap;
import java.util.LinkedList;
//TODO: remove bet from database
public class Bet{
    private String m_betID;
    private String m_home_team_score;
    private String m_away_team_score;
    private LinkedList<String> m_who_scored;
    private String m_num_of_yellow_cards;
    private String m_num_of_red_cards;

    public void setAllBet(HashMap<String, String> bets){

    }
    public void setBetID(String m_betID) {
        this.m_betID = m_betID;
    }

    public void setHomeTeamScore(String home_team_score) {
        this.m_home_team_score = m_home_team_score;
    }

    public void setAwayTeamScore(String away_team_score) {
        this.m_away_team_score = away_team_score;
    }

    public void setWhoScored(LinkedList<String> who_scored) {
        this.m_who_scored = who_scored;
    }

    public void setNumOfYellowCards(String num_of_yellow_cards) {
        this.m_num_of_yellow_cards = num_of_yellow_cards;
    }

    public void setNumOfRedCards(String num_of_red_cards) {
        this.m_num_of_red_cards = num_of_red_cards;
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
