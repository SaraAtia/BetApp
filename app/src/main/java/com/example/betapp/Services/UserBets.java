package com.example.betapp.Services;

import java.util.LinkedList;
import java.util.List;

public class UserBets {
    private LinkedList<Bet> userGuesses;
    private int userScore;
    private String id;

     public UserBets(String id){
         this.id = id;
         this.userGuesses = new LinkedList<>();
         this.userScore = 0;
     }

    public String getId() {
        return id;
    }

    public void addBetToList(Bet bet){
         this.userGuesses.add(bet);
     }

     public void removeBetFromList(Bet bet){
         this.userGuesses.remove(bet);
     }

     public LinkedList<Bet> getUserGuesses(){
         return this.userGuesses;
     }

     public void addPointsToUserProfile(float points){
         this.userScore+=points;
     }


    public int getUserScore(){
         return this.userScore;
    }

    public Bet getBetByMatchName(String match){
         for(Bet bet: this.getUserGuesses()){
             if(bet.getMatch() == match){
                 return bet;
             }
         }
         return null;
    }

}
