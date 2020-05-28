package com.example.betapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betapp.Services.Bet;
import com.example.betapp.Services.Game;
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
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Gamble extends AppCompatActivity {

    ArrayList<String> playersFromPopupWindow = popupPlayersList.players;


    TextView gameString;
    Button submitButton, scoredForHomeButton, scoredForAwayButton;
    EditText homeScore, awayScore, yellowCards, redCards;
 //   ArrayList<String> who_scored = new ArrayList<>();
    String gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);
        gameID = getIntent().getStringExtra("gameID");

        gameString = (TextView)findViewById(R.id.game_str);
        try {
            gameString.setText((String)HttpService.getInstance().getJSON(Consts.GAMES_DATABASE)
                    .getJSONObject(gameID).get("mGame_name"));
        } catch (JSONException|ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }
        submitButton = (Button)findViewById(R.id.submit_btn);
        scoredForHomeButton = (Button)findViewById(R.id.scored_for_home_team);
        scoredForAwayButton = (Button)findViewById(R.id.scored_for_away_team);
        homeScore = (EditText)findViewById(R.id.score_home_team_bet);
        awayScore = (EditText)findViewById(R.id.score_away_team_bet);
        yellowCards = (EditText)findViewById(R.id.yellow_cards_bet);
        redCards = (EditText)findViewById(R.id.red_cards_bet);
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

    public void submitBet(View view){
        Bet bet = new Bet(this.homeScore.getText().toString(),this.awayScore.getText().toString(),
                this.yellowCards.getText().toString(), this.redCards.getText().toString());
        Bet.uploadToDB(bet);
        String betID = bet.getmBetID();
        String userID = AuthActivity.mUser.user_ID;
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference curr_game_ref = DB.getReference("games").child(gameID);
        DatabaseReference user_bets = curr_game_ref.child("mUsers_bets");
        user_bets.child(userID).setValue(betID);
        Toast.makeText(this, "Bet is saved", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AllGamesPresentation.class);
        try {
            String groupID = (String) HttpService.getInstance().getJSON(Consts.GAMES_DATABASE).
                    getJSONObject(gameID).get("mGroupID");
            intent.putExtra("groupID", groupID);
        } catch (JSONException|ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    public JSONObject getPlayersByTeamID(String teamId)throws ExecutionException,
            InterruptedException, JSONException {
        return HttpService.getInstance().getJSON(Consts.PLAYERS_BY_TEAM_ID + teamId);
    }

}

