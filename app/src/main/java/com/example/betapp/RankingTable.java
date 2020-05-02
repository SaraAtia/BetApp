package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.betapp.Services.Bet;
import com.example.betapp.Services.Group;
//import com.example.betapp.Services.UserBets;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class RankingTable extends AppCompatActivity {
    private Group group;
    private LinkedHashMap<String, Float> finalRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_table);
        TableLayout victory_table = (TableLayout)findViewById(R.id.victory_table);
//        victory_table.setStretchAllColumns(true);
//        victory_table.bringToFront();
//
//
//        //user1
//        LinkedList<String> bet1_home_match_1_players = new LinkedList<>();
//        bet1_home_match_1_players.add("david");
//        bet1_home_match_1_players.add("eli");
//        bet1_home_match_1_players.add("eli");
//
//
//        LinkedList<String> bet1_home_match_2_players = new LinkedList<>();
//
//        Bet bet1a = new Bet("a vs b", 3, 2, bet1_home_match_1_players,
//                2, 0);
//
//        UserBets userBets1 = new UserBets("user 1");
//        userBets1.addBetToList(bet1a);
//
//        //user2
//        LinkedList<String> bet2_home_match_1_players = new LinkedList<>();
//        bet2_home_match_1_players.add("eden");
//        bet2_home_match_1_players.add("dekel");
//
//
//        Bet bet1b = new Bet("a vs b", 3, 0, bet2_home_match_1_players,
//                3, 1);
//
//        UserBets userBets2 = new UserBets("user 2");
//        userBets2.addBetToList(bet1b);
//
//
//        userBets1.addPointsToUserProfile(150);
//        userBets2.addPointsToUserProfile(120);
//
//        group = new Group(123);
//        group.addUserToGroup(userBets1);
//        group.addUserToGroup(userBets2);
//
//        finalRank = new LinkedHashMap<>();
//
//        finalRank.put(userBets1.getId(),userBets1.getUserScore());
//        finalRank.put(userBets2.getId(),userBets2.getUserScore());
//
//
//
//        int i = 1;
//        for(int j = 0; j < 40; j++) {
//            for (String s : finalRank.keySet()) {
//                TableRow tr = new TableRow(this);
//                tr.setBackgroundColor(Color.parseColor("#000000"));
//                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                        0, 1));
//
//                UserBets userBets = group.getUserByID(s);
//                Bet bet = userBets.getBetByMatchName("a vs b");
//
//                TextView rank = createTextView(String.valueOf(i++), 0);
//                TextView userName = createTextView(s, 1);
//                TextView matchScore = createTextView(bet.getHomeTeamScore() + " - " + bet.getAwayTeamScore(), 2);
//                TextView scoringPlayers = createTextView(bet.scoringPlayersToString(), 3);
//                TextView yellowCards = createTextView(String.valueOf(bet.getNumOYellowCards()), 4);
//                TextView redCards = createTextView(String.valueOf(bet.getNumOfRedCards()), 5);
//
//                tr.addView(rank);
//                tr.addView(userName);
//                tr.addView(matchScore);
//                tr.addView(scoringPlayers);
//                tr.addView(yellowCards);
//                tr.addView(redCards);
//
//                victory_table.addView(tr);
//            }
//        }

    }

    public TextView createTextView(String text, int columnText){
        final TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        tv.setLayoutParams(new TableRow.LayoutParams(columnText));
        tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, android.R.style.TextAppearance_Large);
        tv.setText(text);
        tv.setTextSize(17);
        TableRow.LayoutParams tvParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        tvParams.setMargins(1, 1, 1 , 1);
        tv.setLayoutParams(tvParams);
        return tv;
    }

//    public LinkedHashMap<String, Float> sortMap(LinkedHashMap<String, Float> unsortedMap){
//        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();
//        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).
//                forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
//        return sortedMap;
//    }
}
