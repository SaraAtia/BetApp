package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class GeneralRankingTable extends AppCompatActivity {

    private String mGroupID;
    private TableLayout mRanking_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_ranking_table);
        mGroupID = getIntent().getStringExtra("groupID");
        mRanking_table = findViewById(R.id.general_ranking_table);
        mRanking_table.setStretchAllColumns(true);
        mRanking_table.bringToFront();
        readDataFromDB();
    }

    /**
     * Get info from DB for filling ranking table.
     */
    private void readDataFromDB(){
        try {
            JSONObject game_info = HttpService.getInstance().getJSON(Consts.GROUPS_DATABASE).
                    getJSONObject(mGroupID);
            JSONObject users_info = HttpService.getInstance().getJSON(Consts.USERS_DATABASE);
            JSONArray ranking_info = game_info.getJSONArray("ranking_table");
            int info_len = ranking_info.length();
            for (int i = 1; i <= info_len; i++) {
                JSONObject user_rank = ranking_info.getJSONObject(i);
                String userID = user_rank.keys().next();
                String user_name = users_info.getJSONObject(userID).getString("user_name");
                createRow(String.valueOf(i), user_name);
            }
        } catch (JSONException | ExecutionException |InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a row in ranking table.
     * @param rank
     * @param userName
     */
    private void createRow (String rank, String userName){
        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.parseColor("#000000"));
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                0, 1));

        TextView rankView = RankingTable.createTextView(this, rank, 0);
        TextView userNameView = RankingTable.createTextView(this, userName, 1);

        tr.addView(rankView);
        tr.addView(userNameView);

        mRanking_table.addView(tr);
    }

    /**
     * When clicking back button on device - return to myGroups.
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MyGroups.class);
        startActivity(intent);
    }
}
