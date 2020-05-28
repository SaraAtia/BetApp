package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.betapp.Services.Game;

import java.util.HashMap;

public class GamePresentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_presentation);
        String gameID = getIntent().getStringExtra("gameID");
        final Game curr_game = Game.getGame(gameID);
        if(curr_game!=null) {
            TextView game_name_view = (TextView) findViewById(R.id.game_name);
            game_name_view.setText(curr_game.mGame_name);
            showGameDetails(curr_game);
            final Context context = this;
            LinearLayout layout = (LinearLayout) findViewById(R.id.game_present_layout);
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
                layout.addView(gamble_btn);
            } else {
                //TODO: get game's results
                Button ranking_btn = new Button(this);
                ranking_btn.setText(R.string.ranking_table);
                ranking_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RankingTable.class);
                        startActivity(intent);
                    }
                });
                layout.addView(ranking_btn);
            }
        }

    }

    private void showGameDetails(Game game){
//        HashMap<String, String> game_details = Game.getGameDetails(game.mGameID_API);
        //TODO: check the function getGameDetails
//        ((TextView)findViewById(R.id.game_league)).setText(game_details.get("strLeague"));
        ((TextView)findViewById(R.id.game_date)).setText(game.mDate);
    }
}
