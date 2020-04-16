package com.example.betapp.Services;

import java.util.LinkedList;

public class LiveScoreGames {
    private LinkedList<SoccerGame> liveScoreSoccerGames;
    public LiveScoreGames(){
        this.liveScoreSoccerGames = new LinkedList<>();
    }

    public LinkedList<SoccerGame> getLiveScoreSoccerGames() {
        return liveScoreSoccerGames;
    }

    public void addGame(SoccerGame soccerGame){
        this.liveScoreSoccerGames.add(soccerGame);
    }

    public SoccerGame getSoccerGameByName(String match){
        for(SoccerGame soccerGame: this.liveScoreSoccerGames){
            if(soccerGame.getMatch() == match){
                return soccerGame;
            }
        }
        return null;
    }
}
