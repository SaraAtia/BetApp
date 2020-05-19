package com.example.betapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betapp.Services.Game;
import com.example.betapp.Services.Group;
import com.example.betapp.Services.HttpService.HttpService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Gamble extends AppCompatActivity {

    ArrayList<String> playersFromPopupWindow = popupPlayersList.players;


    TextView gameString;
    Button nextButton, prevButton, scoredForHomeButton, scoredForAwayButton;
    EditText homeScore, awayScore, yellowCards, redCards;
    int counter = 0;
    final HashMap<String, HashMap<String,String>> gamesDetails = new HashMap<>(); // gameID: {field:field_val}
    final HashMap<Integer, String> games_data_by_SN = new HashMap<>(); // serial_num: gameID
    int numOfGames;

    final ArrayList<String> home_score_arr = new ArrayList<>();
    final ArrayList<String> away_score_arr = new ArrayList<>();
    final ArrayList<ArrayList<String>> scoring_players_arr = new ArrayList<>();
    final ArrayList<String> yellow_cards_arr = new ArrayList<>();
    final ArrayList<String> red_cards_arr = new ArrayList<>();


    //String: user name, JSONOBJECT: user's bets
    private static LinkedHashMap<String, JSONObject> userBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);
       // String groupID = getIntent().getStringExtra("groupID");
        gameString = (TextView)findViewById(R.id.game_str);
        nextButton = (Button)findViewById(R.id.next_game);
        prevButton = (Button)findViewById(R.id.previous_game);
        scoredForHomeButton = (Button)findViewById(R.id.scored_for_home_team);
        scoredForAwayButton = (Button)findViewById(R.id.scored_for_away_team);
        homeScore = (EditText)findViewById(R.id.score_home_team_bet);
        awayScore = (EditText)findViewById(R.id.score_away_team_bet);
        yellowCards = (EditText)findViewById(R.id.yellow_cards_bet);
        redCards = (EditText)findViewById(R.id.red_cards_bet);
        //todo: change next line to -  getGamesByGroupID(groupID);
        getGamesByGroupID("-M7h40hrWiloAjKHwVM7");
    }

    public void setNextGameParameters(){
        String gameID = games_data_by_SN.get(counter);
        String gameS = gamesDetails.get(gameID).get("strEvent");
        gameString.setText(gameS);
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
            updateAllArrays();
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

    /**
     * Read from DB group's games to bet on and read the games data from API.
     * @param groupID groupID on DB
     */
    public void getGamesByGroupID(final String groupID){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // get the map between user id in authentication to user entry in user's database
        DatabaseReference groupsDB = database.getReference("groups");
        groupsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot groupSnapshot =  dataSnapshot.child(groupID);
                HashMap<String, String> group_games = (HashMap<String, String>)groupSnapshot.child("games").getValue();
                final Set games_ID = group_games.keySet();
                DatabaseReference gamesDB = database.getReference("games");
                gamesDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (Object gameID: games_ID) {
                            String id = (String) gameID;
                            Game g = dataSnapshot.child((String) gameID).getValue(Game.class);
                            gamesDetails.put(id, Game.getGameDetails(g.mGameID_API));
                            games_data_by_SN.put(count, id);
                            count++;
                        }
                        numOfGames = gamesDetails.size();
                        ArrayList<ArrayList<String>> temp_arr = new ArrayList<>(numOfGames);
                        away_score_arr.addAll(Collections.nCopies(numOfGames, "Away Score"));
                        home_score_arr.addAll(Collections.nCopies(numOfGames, "Home Score"));
                        scoring_players_arr.addAll(temp_arr);
                        yellow_cards_arr.addAll(Collections.nCopies(numOfGames, "Yellow"));
                        red_cards_arr.addAll(Collections.nCopies(numOfGames, "Red"));
                        setNextGameParameters();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public JSONObject getPlayersByTeamID(String teamId)throws ExecutionException,
            InterruptedException, JSONException {
        return HttpService.getInstance().getJSON(Consts.PLAYERS_BY_TEAM_ID + teamId);
    }

}

