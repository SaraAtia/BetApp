package com.example.betapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class Gamble extends AppCompatActivity {

    ArrayList<String> playersFromPopupWindow = popupPlayersList.players;


    TextView gameString;
    Button nextButton, prevButton, scoredForHomeButton, scoredForAwayButton;
    EditText homeScore, awayScore, yellowCards, redCards;
    int counter = 0;

    LinkedHashMap<Integer, LinkedHashMap<String,String>> gamesDetails = getGamesByGroupId("gsi");
    int numOfGames = gamesDetails.size();

    ArrayList<String> home_score_arr = new ArrayList<>(Collections.nCopies(numOfGames, "Home Score"));
    ArrayList<String> away_score_arr = new ArrayList<>(Collections.nCopies(numOfGames, "Away Score"));
    ArrayList<ArrayList<String>> scoring_players_arr = new ArrayList<>(numOfGames);
    ArrayList<String> yellow_cards_arr = new ArrayList<>(Collections.nCopies(numOfGames, "Yellow"));
    ArrayList<String> red_cards_arr = new ArrayList<>(Collections.nCopies(numOfGames, "Red"));


    //String: user name, JSONOBJECT: user's bets
    private static LinkedHashMap<String, JSONObject> userBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);

        gameString = (TextView)findViewById(R.id.game_str);
        nextButton = (Button)findViewById(R.id.next_game);
        prevButton = (Button)findViewById(R.id.previous_game);
        scoredForHomeButton = (Button)findViewById(R.id.scored_for_home_team);
        scoredForAwayButton = (Button)findViewById(R.id.scored_for_away_team);
        homeScore = (EditText)findViewById(R.id.score_home_team_bet);
        awayScore = (EditText)findViewById(R.id.score_away_team_bet);
        yellowCards = (EditText)findViewById(R.id.yellow_cards_bet);
        redCards = (EditText)findViewById(R.id.red_cards_bet);

        setNextGameParameters();
    }

    public void setNextGameParameters(){
        String gameS = ((gamesDetails.get(counter)).get("strEvent"));
        gameString.setText((gamesDetails.get(counter)).get("strEvent"));
        if(home_score_arr.get(counter).equals("Home Score") && away_score_arr.get(counter).equals("Away Score") && yellow_cards_arr.get(counter).equals("Yellow") &&
        red_cards_arr.get(counter).equals("Red")){ //TODO:change condition, find a way to include players, or find a way to short the condition.
            homeScore.setHint(home_score_arr.get(counter));
            awayScore.setHint(away_score_arr.get(counter));
            yellowCards.setHint(yellow_cards_arr.get(counter));
            redCards.setHint(red_cards_arr.get(counter));
            homeScore.setText("");
            awayScore.setText("");
            yellowCards.setText("");
            redCards.setText("");
        }
        else {
            homeScore.setText(home_score_arr.get(counter));
            awayScore.setText(away_score_arr.get(counter));
            yellowCards.setText(yellow_cards_arr.get(counter));
            redCards.setText(red_cards_arr.get(counter));
        }
    }

    public void updateParameter(String currentValue, ArrayList<String> arr){
        if(!arr.get(counter).equals(currentValue)){
            arr.set(counter,currentValue);
        }
    }

    public void updateAllArrays(){
        updateParameter(homeScore.getText().toString(), home_score_arr);
        updateParameter(awayScore.getText().toString(), away_score_arr);
        updateParameter(yellowCards.getText().toString(), yellow_cards_arr);
        updateParameter(redCards.getText().toString(), red_cards_arr);
    }


    public void changeNextButtonString(){
        if(!nextButton.getText().toString().equals("next game")){
            nextButton.setText("next game");
        }
    }


    public void homeTeamPlayers(View view){
//        try{
//            JSONObject home_team_players = getPlayersByTeamID((gamesDetails.get(counter)).get("idHomeTeam"));
//        }catch (Exception e){
//            System.out.println("GambleWin line 108"); //TODO: change exception
//        }
        startActivity(new Intent(Gamble.this, popupPlayersList.class));
    }

    public void awayTeamPlayers(View view){
//        try{
//            JSONObject home_team_players = getPlayersByTeamID((gamesDetails.get(counter)).get("idAwayTeam"));
//        }catch (Exception e){
//            System.out.println("GambleWin line 108"); //TODO: change exception
//        }
        startActivity(new Intent(Gamble.this, popupPlayersList.class));
    }

    public void nextGame(View view){

        if(nextButton.getText().toString().equals("submit")){
            Toast.makeText(this, "data if saved in DB", Toast.LENGTH_LONG).show();
            for(String s: home_score_arr){
                System.out.println(s);
            }
            for(String s: away_score_arr){
                System.out.println(s);
            }
            for(String s: yellow_cards_arr){
                System.out.println(s);
            }
            for(String s: red_cards_arr){
                System.out.println(s);
            }
        }
        else{
            if(counter<numOfGames-1){
                if(homeScore.getText().toString().equals("") || awayScore.getText().toString().equals("") ||
                        yellowCards.getText().toString().equals("") || redCards.getText().toString().equals("")){
                    Toast.makeText(this, "missing fields - next", Toast.LENGTH_LONG).show();
                }
                else{
                    updateAllArrays();
                    counter++;
                    setNextGameParameters();
                }
            }
            else {
                nextButton.setText("submit");
                Toast.makeText(this, "there isn't next game to bet on"+"\n"+" you can submit your bets",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void previousGame(View view){
        if(counter>0){
            changeNextButtonString();
            if(homeScore.getText().toString().equals("") || awayScore.getText().toString().equals("") ||
                    yellowCards.getText().toString().equals("")||redCards.getText().toString().equals("")){
                Toast.makeText(this, "missing fields - prev", Toast.LENGTH_LONG).show();
            }
            else {
                updateAllArrays();
                counter--;
                setNextGameParameters();
            }
        }
        else {
            Toast.makeText(this, "there isn't prev game to bet on", Toast.LENGTH_LONG).show();
        }
    }

    public LinkedHashMap<Integer, LinkedHashMap<String,String>> getGamesByGroupId(String groupId){
        LinkedHashMap<Integer, LinkedHashMap<String,String>> details = new LinkedHashMap<>(3);
        LinkedHashMap<String,String> game1 = new LinkedHashMap<>();
        game1.put("idEvent", "602439");
        game1.put("strEvent", "Aston Villa vs Wolves");
        game1.put("strHomeTeam", "Aston Villa");
        game1.put("strAwayTeam", "Wolves");
        game1.put("idHomeTeam", "133601");
        game1.put("idAwayTeam", "133599");
        LinkedHashMap<String,String> game2 = new LinkedHashMap<>();
        game2.put("idEvent", "602441");
        game2.put("strEvent", "Bournemouth vs Newcastle");
        game2.put("strHomeTeam", "Bournemouth");
        game2.put("strAwayTeam", "Newcastle");
        game2.put("idHomeTeam", "134301");
        game2.put("idAwayTeam", "134777");
        LinkedHashMap<String,String> game3 = new LinkedHashMap<>();
        game3.put("idEvent", "602443");
        game3.put("strEvent", "Brighton vs Man United");
        game3.put("strHomeTeam", "Brighton");
        game3.put("strAwayTeam", "Man United");
        game3.put("idHomeTeam", "133619");
        game3.put("idAwayTeam", "133612");

        details.put(0, game1);
        details.put(1, game2);
        details.put(2, game3);

        return details;
    }


    public JSONObject getPlayersByTeamID(String teamId)throws ExecutionException,
            InterruptedException, JSONException {
        return HttpService.getInstance().getJSON(Consts.PLAYERS_BY_TEAM_ID + teamId);
    }

}

