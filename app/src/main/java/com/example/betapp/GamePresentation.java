package com.example.betapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.betapp.Services.Game;
import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class GamePresentation extends AppCompatActivity {
    private LinearLayout mLayout;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_presentation);
        String gameID = getIntent().getStringExtra("gameID");
        final Game curr_game = Game.getGame(gameID);
        if(curr_game!=null) {
            TextView game_name_view = (TextView) findViewById(R.id.game_name);
            game_name_view.setText(curr_game.mGame_name);
            ((TextView)findViewById(R.id.game_date)).setText(curr_game.mDate);
            try {
                long millis = System.currentTimeMillis();
                java.sql.Date date=new java.sql.Date(millis);
                Date game_date = new SimpleDateFormat("yyyy-MM-dd").parse(curr_game.mDate);
                Date curr_date = new SimpleDateFormat("yyyy-MM-dd").parse(date.toString());
                if(curr_date.equals(game_date)){
                    curr_game.mAvailable_to_bet = false;
                }
                final Context context = this;
                mLayout = (LinearLayout) findViewById(R.id.game_present_layout);
                if(curr_game.mAvailable_to_bet){
                    Button gamble_btn = new Button(this);
                    gamble_btn.setText(R.string.gamble_now);
                    gamble_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Gamble.class);
                            intent.putExtra("gameID", curr_game.mGameID);
                            startActivity(intent);
                        }
                    });
                    mLayout.addView(gamble_btn);
                } else {
                    //todo:check is finish
                    showEventResult(curr_game.mGameID_API);
                    Button ranking_btn = new Button(this);
                    ranking_btn.setText(R.string.ranking_table);
                    ranking_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RankingTable.class);
                            intent.putExtra("gameID", curr_game.mGameID);
                            startActivity(intent);
                        }
                    });
                    mLayout.addView(ranking_btn);
                }
            } catch (ParseException e) {
                e.printStackTrace(); //TODO: logic catch
            }
        }
    }

    /**
     * Present all info of specified game
     * @param game_id_api - specified game id
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showEventResult(String game_id_api){
        try {
            JSONObject event_details = HttpService.getInstance().
                    getJSON(Consts.EVENT_DETAILS+ game_id_api);//TODO: live score url
            String home_score = event_details.getString("intHomeScore");
            String away_score = event_details.getString("intAwayScore");
            String away_red_card = event_details.getString("strAwayRedCards");
            String home_red_card = event_details.getString("strHomeRedCards");
            String away_yellow_card = event_details.getString("strAwayYellowCards");
            String home_yellow_card = event_details.getString("strHomeYellowCards");
            ArrayList<String> home_players_scored = getPlayersScored(event_details.
                    getJSONObject("strHomeGoalDetails"));
            String home_players_scored_str = String.join(",",home_players_scored);
            ArrayList<String> away_players_scored = getPlayersScored(event_details.
                    getJSONObject("strAwayGoalDetails"));
            String away_players_scored_str = String.join(",",away_players_scored);
            createTextView("Home Team Score: ", home_score, true);
            createTextView("Away Team Score: " , away_score, true);
            createTextView("Home Team Red Cards: ", home_red_card, true);
            createTextView("Away Team Red Cards: ", away_red_card, true);
            createTextView("Home Team Yellow Cards: ", home_yellow_card, true);
            createTextView("Away Team Yellow Cards: ", away_yellow_card, true);
            createTextView("Home Team Players Scored: ", home_players_scored_str, false);
            createTextView("Away Team Players Scored: ", away_players_scored_str, false);
        } catch (JSONException|InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void createTextView(String fieldName, String fieldValue, boolean isNum){
        if(fieldValue.isEmpty()){
            if(isNum){
                fieldValue = "0";
            } else{
                return;
            }
        }
        TextView textView = new TextView(this);
        textView.setText(fieldName + fieldValue);
        textView.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mLayout.addView(textView);

    }

    /**
     * Extract from JSON of players who scored - players name as array.
     * @param players_scoredJSON
     * @return array list of all players who scored
     * @throws JSONException
     */
    private ArrayList<String> getPlayersScored(JSONObject players_scoredJSON) throws JSONException {
        ArrayList<String> players_scored = new ArrayList<>();
        for (Iterator<String> it = players_scoredJSON.keys(); it.hasNext(); ) {
            String key = it.next();
            String player_scored = players_scoredJSON.getString(key);
            players_scored.add(player_scored);
        }
        return players_scored;
    }
}
