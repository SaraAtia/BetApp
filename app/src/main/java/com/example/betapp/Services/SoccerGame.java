package com.example.betapp.Services;

import java.util.LinkedList;

public class SoccerGame {
    private String match;
    private int homeTeamScore;
    private int awayTeamScore;
    private LinkedList<String> whoScored;
    private int numOYellowCards;
    private int numOfRedCards;
    private boolean isFinished;

    public SoccerGame(String match, int homeTeamScore, int awayTeamScore, LinkedList<String> whoScored
            ,int numOYellowCards, int numOfRedCards, boolean isFinished){
        this.match = match;
        this.homeTeamScore=homeTeamScore;
        this.awayTeamScore= awayTeamScore;
        this.whoScored = whoScored;
        this.numOYellowCards=numOYellowCards;
        this.numOfRedCards = numOfRedCards;
        this.isFinished = isFinished;
    }

    public String getMatch(){
        return this.match;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getNumOfRedCards() {
        return numOfRedCards;
    }

    public int getNumOYellowCards() {
        return numOYellowCards;
    }

    public boolean getIsFinished(){
        return isFinished;
    }

    public int whoWon(){
        if(this.isFinished){
            if(this.homeTeamScore>this.awayTeamScore){
                return 1;
            }
            if(this.awayTeamScore>this.homeTeamScore){
                return 2;
            }
            return 0;
        }
        return -1;
    }

    public LinkedList<String> getWhoScored() {
        return whoScored;
    }

    public void addScoringPlayer(String playerScoredName){
        this.whoScored.add(playerScoredName);
    }


}
