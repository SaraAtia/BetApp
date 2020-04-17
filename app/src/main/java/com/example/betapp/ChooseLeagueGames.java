package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.betapp.Services.GamesAdapter;
import com.example.betapp.Services.HttpService.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import static com.example.betapp.Consts.GROUPS_DATABASE;
import static com.example.betapp.Consts.MAX_GAMES_TO_BET;



public class ChooseLeagueGames extends AppCompatActivity {
    private final GamesList m_gamesListJSON = new GamesList(MAX_GAMES_TO_BET);
    private ArrayList<Button> m_itemsArr;
    private JSONArray m_events;
    ListView list;
    private Intent m_bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_league_games);
        Intent intent = getIntent();
        m_bundle = intent;
        String leagueId = intent.getStringExtra("leagueID");
        list = (ListView) findViewById(R.id.games_options_layout);
        try {
            JSONObject jo = getNext15Games(leagueId);
            System.out.println("NEXT 15 GAMES:" + jo.toString());
            this.m_events = (JSONArray) jo.get("events");
            this.m_itemsArr = createCheckboxes(m_events, leagueId);
            createSubmitBtn();
            GamesAdapter gamesAdapter = new GamesAdapter(this, m_itemsArr, m_events, m_gamesListJSON);
            gamesAdapter.notifyDataSetChanged();
            list.setAdapter(gamesAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((GamesAdapter) list.getAdapter()).notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            System.out.println(this.toString() + " line 85");
            e.printStackTrace();
        }
        /*if(flag_restart && Integer.valueOf(leagueId).equals(Integer.valueOf(savedInstanceState.getString("leagueId")))){
            for (int i = 0; i < m_itemsArr.size()-1; i++){
                ((CheckBox)m_itemsArr.get(i)).setChecked(savedInstanceState.getBoolean
                        (m_itemsArr.get(i).getText().toString()));
            }
        }*/
    }

    public JSONObject getNext15Games(String leagueId) throws ExecutionException, InterruptedException, JSONException {
        Response response;
        response = HttpService.getInstance().sendRequest("GET",
                Consts.NEXT15EVENTS_BY_LEAGUEID + leagueId, null);
        String responseStr = "";
        if (response.code() == HttpsURLConnection.HTTP_OK) {
            try {
                responseStr = response.body().string();
                System.out.println(responseStr);

            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
                System.out.println(this.toString() + " line 101");
            }
        }
        // parsing file "JSONExample.json"
        return new JSONObject(responseStr);
    }

    public ArrayList<Button> createCheckboxes(JSONArray events, String leagueId) {
        boolean has_status = false;
        Boolean[] status = null;
        try {
            ArrayList<Button> boxes = new ArrayList<>();
            if(CreateGroup.chosenGames.containsKey(leagueId)){
                has_status = true;
                status = CreateGroup.chosenGames.get(leagueId);
            }
          /*  for(int i = 0; i<15; i++){
                checkbox_status[i] = (Boolean) extras.get(leagueId+"_"+i);
            }*/
            for (int i = 0; i < events.length(); i++) {
                final Button event = new CheckBox(this);
                event.setLayoutParams(new ListView.LayoutParams
                        (ListView.LayoutParams.WRAP_CONTENT,
                                ListView.LayoutParams.WRAP_CONTENT));
                if(has_status){
                    ((CheckBox)event).setChecked(status[i]);
                }
                event.setId(i);
                event.setText(events.getJSONObject(i).getString("strEvent"));
                event.setPadding(1, 1, 1, 1);
                event.setTextSize(15);


                boxes.add(event);
            }
            return boxes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createSubmitBtn() {
        Button submit_btn = new Button(this);
        submit_btn.setId(this.m_itemsArr.size());
        submit_btn.setLayoutParams(new ListView.LayoutParams
                (ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));
        submit_btn.setText(R.string.submit);
        this.m_itemsArr.add(submit_btn);
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        if (savedInstanceState == null) savedInstanceState = m_bundle.getExtras();
//        // Restore UI state using savedInstanceState.
//        for (int i = 0; i < m_itemsArr.size()-1; i++){
//            ((CheckBox)m_itemsArr.get(i)).setChecked(savedInstanceState.getBoolean
//                    (m_itemsArr.get(i).getText().toString()));
//        }
//
//    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
        Intent intent = getIntent();
        String leagueId = intent.getStringExtra("leagueID");
        if(leagueId != null){
            Boolean[] checkbox_status = ((GamesAdapter) list.getAdapter()).getCheckedMap().values().toArray(new Boolean[15]);
            CreateGroup.chosenGames.put(leagueId, checkbox_status);
        }
    }
    @Override
    public void onBackPressed(){
        onSaveInstanceState(m_bundle.getExtras());
        super.onBackPressed();
    }
}