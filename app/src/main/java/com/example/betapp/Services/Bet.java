package com.example.betapp.Services;

import java.util.LinkedList;

public class Bet {
    private String match;
    private int homeTeamScore;
    private int awayTeamScore;
    private LinkedList<String> whoScored;
    private int numOYellowCards;
    private int numOfRedCards;
    private int winningTeam;

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


    public String getMatch() {
        return match;
    }

    public int getWinningTeam(){
        return this.winningTeam;
    }
}
