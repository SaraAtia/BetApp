package com.example.betapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Gamble extends AppCompatActivity {

    ArrayList<String> playersFromPopupWindow = PopupPlayersList.players;

    TextView gameString;
    Button submitButton, scoredForHomeButton, scoredForAwayButton;
    EditText homeScore, awayScore, yellowCards, redCards;
    String away_teamID, home_teamID;
    public static HashMap<String, ArrayList<CheckBox>> players_view_by_teamID;
    public static final HashMap<String, HashMap<Integer, Boolean>> teamPlayersChecked =
            new HashMap<>(2);//teamID:checked players(int=position, boolean=isChecked)
    String gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamble);
        gameID = getIntent().getStringExtra("gameID");
        gameString = (TextView)findViewById(R.id.game_str);
        players_view_by_teamID = new HashMap<>();
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
        intent.putExtra("teamID", this.home_teamID);
        startActivity(intent);
    }

    public void awayTeamPlayers(View view){
        Intent intent = new Intent(Gamble.this, PopupPlayersList.class);
        intent.putExtra("teamID", this.away_teamID);
        startActivity(intent);
    }

    /**
     * Create a map containing all info of players selected from each team
     * @return map with players selected info {player_name:player_id}
     */
    private HashMap<String, String> getSelectedPlayers(String teamID){
        HashMap<String, String> players_selected = new HashMap<>();
        HashMap<Integer, Boolean> players_by_teamID= teamPlayersChecked.get(teamID);
        ArrayList<CheckBox> players_info_by_teamID = players_view_by_teamID.get(teamID);
        if (players_by_teamID == null){
            teamPlayersChecked.remove(teamID);
            return new HashMap<String, String>();
        }
        for(Map.Entry<Integer, Boolean> entry: players_by_teamID.entrySet()){
            if(entry.getValue()){
                CheckBox player_checkbox = players_info_by_teamID.get(entry.getKey());
                players_selected.put(player_checkbox.getTag().toString(), player_checkbox.getText().toString());
            }
        }
        return players_selected;
    }

    /**
     * Save bet info and upload to DB.
     * @param view submit btn
     */
    public void submitBet(View view){
        Bet bet = new Bet(this.homeScore.getText().toString(),this.awayScore.getText().toString(),
                this.yellowCards.getText().toString(), this.redCards.getText().toString(),
                getSelectedPlayers(this.home_teamID), getSelectedPlayers(this.away_teamID));
        Bet.uploadToDB(bet);
        String betID = bet.getmBetID();
        String userID = AuthActivity.mUser.userID;
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

