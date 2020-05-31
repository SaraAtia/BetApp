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
import com.example.betapp.Services.HttpService.HttpService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Gamble extends AppCompatActivity {

    ArrayList<String> playersFromPopupWindow = PopupPlayersList.players;

    TextView gameString;
    Button submitButton, scoredForHomeButton, scoredForAwayButton;
    EditText homeScore, awayScore, yellowCards, redCards;
    String away_teamID, home_teamID;
 //   ArrayList<String> who_scored = new ArrayList<>();
    String gameID;
//Todo: not allow submit a bet with an empty field
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);
        gameID = getIntent().getStringExtra("gameID");

        gameString = (TextView)findViewById(R.id.game_str);
        try {
            JSONObject game_details = HttpService.getInstance().getJSON(Consts.GAMES_DATABASE)
                    .getJSONObject(gameID);
            this.away_teamID = (String) game_details.get("away_teamID");
            this.home_teamID = (String) game_details.get("home_teamID");
            gameString.setText((String)game_details.get("mGame_name"));
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
        Intent intent = new Intent(Gamble.this, PopupPlayersList.class);
        intent.putExtra("away_teamID", this.away_teamID);
        intent.putExtra("home_teamID", this.home_teamID);
        startActivity(intent);
    }

    public void awayTeamPlayers(View view){
        Intent intent = new Intent(Gamble.this, PopupPlayersList.class);
        intent.putExtra("away_teamID", this.away_teamID);
        intent.putExtra("home_teamID", this.home_teamID);
        startActivity(intent);
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



}

