package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.betapp.Services.GamesAdapter;
import com.example.betapp.Services.HttpService.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import static com.example.betapp.Consts.MAX_GAMES_TO_BET;


/**
 * Choose league games activity present 15 closest games of league chosen and let
 * group manger (user) to choose up to MAX_GAMES_TO_BET games.
 * Those are the games which group will bet on.
 */
public class ChooseLeagueGames extends AppCompatActivity {
    private ArrayList<Button> m_itemsArr;
    private JSONArray m_events;
    private ListView list;
    private Intent m_bundle;

    /**
     * Init all screen object - game
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_league_games);
        Intent intent = getIntent();
        m_bundle = intent;
        String leagueId = intent.getStringExtra("leagueID");
        list = (ListView) findViewById(R.id.games_options_layout);
        this.m_itemsArr = new ArrayList<>();
        try {
            JSONObject next15Games = HttpService.getInstance().
                    getJSON(Consts.NEXT15EVENTS_BY_LEAGUEID + leagueId);

            this.m_events = (JSONArray) next15Games.get("events"); //JSON with 15 next games.
            // create checkbox for each game
            this.m_itemsArr = createCheckboxes(m_events, leagueId);
        } catch (Exception e) {
            System.out.println(this.toString() + " line 71");
            e.printStackTrace();
        }
        createSubmitBtn();
        // adapter contains all buttons to be presented in scrolled list
        GamesAdapter gamesAdapter = new GamesAdapter(this, m_itemsArr, CreateGroup.chosenGames, leagueId);
        list.setAdapter(gamesAdapter); // update list of it's adapter
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((GamesAdapter) list.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    /**
     * create a checkbox for each game event of the league.
     * @param events games info
     * @param leagueId string id present the league in DB
     * @return array of all checkboxes created
     */
    public ArrayList<Button> createCheckboxes(JSONArray events, String leagueId) {
        boolean has_status = false;
        Boolean[] status = null;
        try {
            ArrayList<Button> boxes = new ArrayList<>();
            // if games from league already chosen - turn on flag and get a boolean list
            // presents the game's checkbox status. check if the specific game's checkbox
            // is chosen and set its status correspondingly.
            if(CreateGroup.chosenGames.containsKey(leagueId)){
                has_status = true;
                status = CreateGroup.chosenGames.get(leagueId);
            }
            for (int i = 0; i < events.length(); i++) {
                final Button event = new CheckBox(this);
                event.setLayoutParams(new ListView.LayoutParams
                        (ListView.LayoutParams.WRAP_CONTENT,
                                ListView.LayoutParams.WRAP_CONTENT));
                if(has_status){ // if game is chosen set checked = true
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

    /**
     * create a submit btn and set its value
     */
    public void createSubmitBtn() {
        Button submit_btn = new Button(this);
        submit_btn.setId(this.m_itemsArr.size());
        submit_btn.setLayoutParams(new ListView.LayoutParams
                (ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));
        submit_btn.setText(R.string.submit);
        this.m_itemsArr.add(submit_btn);
    }

    /**
     * Before passing to other activity's screen - save data of league's chosen games.
     * Games data will be saved as map
     *      {leagueID:boolean array of game's status(True for game selected)}
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
        Intent intent = getIntent();
        String leagueId = intent.getStringExtra("leagueID");
        if(leagueId != null){
            Boolean[] checkbox_status = ((GamesAdapter) list.getAdapter()).
                    getChecked_map().values().toArray(new Boolean[15]);
            CreateGroup.chosenGames.put(leagueId, checkbox_status);
        }
    }

    /**
     * When clicking back button on device - save data's activity first.
     */
    @Override
    public void onBackPressed(){
        onSaveInstanceState(m_bundle.getExtras());
        super.onBackPressed();
    }
}