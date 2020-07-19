package com.example.betapp.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.betapp.AuthActivity;
import com.example.betapp.Consts;
import com.example.betapp.MyGroups;
import com.example.betapp.R;
import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.betapp.App.CHANNEL_ID;

public class NotificationService extends IntentService {
    ArrayList<Game> games;
    public ArrayList<SoccerGame> gamesList;
    int id;
    public HashMap<String, String> groups;
    boolean initialized;
    public static int t = 0;

    public NotificationService()
    {
        super( "NotificationService" );
        this.games = new ArrayList<>();
        this.gamesList = new ArrayList<>();
        this.id = 1;
        this.initialized = false;
    }

    public NotificationService(String name)
    {
        super( "NotificationService" );
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int time = 600000;
        int debugTime = 3000;
        while(true){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.initialized){
                init();
            } else {
                updateGames();
                t++;
            }
        }
    }

    private void notifyGamesResults(String match, int home, int away){
        Intent notificationIntent = new Intent(this, AuthActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(match)
                .setContentText(home + " - " + away)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.notify(id++,notification);
        }
    }

    private void updateGames() {
    try{
        for (int i=0; i<this.games.size();i++) {
            Game game = games.get(i);
            JSONObject gameJSON = HttpService.getInstance().getJSON(
                    Consts.GAME_DETAILS_BY_EVENT_ID + game.mGameID_API);
            String match = gameJSON.getJSONArray("events").getJSONObject(0)
                    .getString("strEvent");
            Object homeScore = gameJSON.getJSONArray("events").getJSONObject(0)
                    .get("intHomeScore");
            Object awayScore = gameJSON.getJSONArray("events").getJSONObject(0)
                    .get("intAwayScore");
            int currHomeScore;
            if (homeScore.toString() != "null") {
                currHomeScore = Integer.parseInt(homeScore.toString());
            } else {
                currHomeScore = 0;
            }

//            if (t>1){
//                currHomeScore += 2;
//            }

            int currAwayScore;
            if (awayScore.toString() != "null") {
                currAwayScore = Integer.parseInt(awayScore.toString());
            } else {
                currAwayScore = 0;
            }

            SoccerGame currentGame = this.gamesList.get(i);
            if (currentGame.getHomeTeamScore() != currHomeScore || currentGame.getAwayTeamScore() != currAwayScore) {
                this.gamesList.get(i).setHomeTeamScore(currHomeScore);
                this.gamesList.get(i).setAwayTeamScore(currAwayScore);
                notifyGamesResults(match, currHomeScore, currAwayScore);
            }
        }
    } catch (Exception e){
        e.printStackTrace();
    }
        }

    private void init(){
        try {
            groups = AuthActivity.mUser.getUserGroups();
        } catch (Exception e){
            return;
        }
        if (groups.isEmpty()){
            return;
        } else {
            Iterator iterator = groups.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry mapElement = (Map.Entry)iterator.next();
                String groupId = mapElement.getValue().toString();
                try {
                    JSONObject group = HttpService.getInstance().getJSON(
                            "https://betapp-7ea26.firebaseio.com/groups/"+
                                    groupId +".json");
                    JSONObject games = group.getJSONObject("games");
                    Iterator<String> keys = games.keys();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        Game game = Game.getGame(key);
                        this.games.add(game);
                        JSONObject gameJSON = HttpService.getInstance().getJSON(
                                Consts.GAME_DETAILS_BY_EVENT_ID+game.mGameID_API);
                        String match = gameJSON.getJSONArray("events").getJSONObject(0)
                                .getString("strEvent");
                        Object homeScore = gameJSON.getJSONArray("events").getJSONObject(0)
                                .get("intHomeScore");
                        Object awayScore = gameJSON.getJSONArray("events").getJSONObject(0)
                                .get("intAwayScore");
                        int currHomeScore;
                        if(homeScore.toString() != "null"){
                            currHomeScore = Integer.parseInt(homeScore.toString());

                        } else {
                            currHomeScore = 0;
                        }
                        int currAwayScore;
                        if(awayScore.toString() != "null"){
                            currAwayScore = Integer.parseInt(awayScore.toString());
                        } else {
                            currAwayScore = 0;
                        }
                        SoccerGame game1 = new SoccerGame(
                                match,currHomeScore,currAwayScore,null,
                                0,0,false);
                        this.gamesList.add(game1);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            this.initialized = true;
        }
    }

    public static void PopupMsg(Context context, String text, int duration){
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
