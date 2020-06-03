package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.betapp.Services.Bet;
import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class RankingTable extends AppCompatActivity {
    private TableLayout mRanking_table;
    private String mGameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_table);
        mGameID = getIntent().getStringExtra("gameID");
        mRanking_table = findViewById(R.id.ranking_table);
        mRanking_table.setStretchAllColumns(true);
        mRanking_table.bringToFront();
        readDataFromDB();
    }

    /**
     * Get info from DB for filling ranking table.
     */
    private void readDataFromDB(){
        try {
            JSONObject game_info = HttpService.getInstance().getJSON(Consts.GAMES_DATABASE).
                    getJSONObject(mGameID);
            JSONObject users_info = HttpService.getInstance().getJSON(Consts.USERS_DATABASE);
            JSONObject bets_info = HttpService.getInstance().getJSON(Consts.BETS_DATABASE);
            JSONArray ranking_info = game_info.getJSONArray("ranking_table");
            int info_len = ranking_info.length();
            for (int i = 1; i <= info_len; i++) {
                JSONObject user_rank = ranking_info.getJSONObject(i);
                String userID = user_rank.keys().next();
                String user_name = users_info.getJSONObject(userID).getString("user_name");
                String betID = game_info.getJSONObject("mUsers_bets").getString(userID);
                Bet bet = new Bet(bets_info.getJSONObject(betID));
                String game_score = bet.mHome_team_score + "-" + bet.mAway_team_score;
                Collection<String> players_scored_collection = new ArrayList<>(bet.mHome_team_players_scored.values());
                Collection<String> away_players_scored_collection = bet.mAway_team_players_scored.values();
                players_scored_collection.addAll(away_players_scored_collection);
                StringBuilder players_scored = new StringBuilder();
                for (Iterator player = players_scored_collection.iterator(); player.hasNext(); ) {
                    players_scored.append(player.next()).append(","); //Todo: delete last ,
                }
                createRow(String.valueOf(i), user_name, game_score, players_scored.toString(),
                        bet.mNum_of_yellow_cards, bet.mNum_of_red_cards);
            }
        } catch (JSONException|ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create a row in ranking table.
     * @param rank
     * @param userName
     * @param score
     * @param scoringPlayers
     * @param yellowCards
     * @param redCards
     */
    private void createRow (String rank, String userName, String score, String scoringPlayers,
                            String yellowCards, String redCards){
        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.parseColor("#000000"));
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                0, 1));

        TextView rankView = createTextView(this, rank, 0);
        TextView userNameView = createTextView(this, userName, 1);
        TextView scoreView = createTextView(this, score, 2);
        TextView scoringPlayersView = createTextView(this, scoringPlayers, 3);
        TextView yellowCardsView = createTextView(this, yellowCards, 4);
        TextView redCardsView = createTextView(this, redCards, 5);

        tr.addView(rankView);
        tr.addView(userNameView);
        tr.addView(scoreView);
        tr.addView(scoringPlayersView);
        tr.addView(yellowCardsView);
        tr.addView(redCardsView);

        mRanking_table.addView(tr);
    }

    public static TextView createTextView(Context c, String text, int columnText) {
        final TextView tv = new TextView(c);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        tv.setLayoutParams(new TableRow.LayoutParams(columnText));
        tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(c, android.R.style.TextAppearance_Large);
        tv.setText(text);
        tv.setTextSize(17);
        TableRow.LayoutParams tvParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        tvParams.setMargins(1, 1, 1, 1);
        tv.setLayoutParams(tvParams);
        return tv;
    }
}
