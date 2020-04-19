package com.example.betapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.betapp.Services.SoccerGame;


import java.util.ArrayList;

import static com.example.betapp.App.CHANNEL_ID;

public class NotificationService extends IntentService {
    ArrayList<SoccerGame> games;

    public NotificationService()
    {
        super( "NotificationService" );
        this.games = new ArrayList<>();
        this.games.add(new SoccerGame("beitar-hapoel",1,0,
                null,0,0,false));
    }

    public NotificationService(String name)
    {
        super( "NotificationService" );
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while(true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateGames();
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
            manager.notify(1,notification);
        }
    }

    private void updateGames(){
        for (int i=0;i<this.games.size();i++){
            int newHome = 5;
            int newAway = 2;
            SoccerGame currentGame = this.games.get(i);
            if (currentGame.getHomeTeamScore() != newHome || currentGame.getAwayTeamScore() != newAway){
                SoccerGame newGame = new SoccerGame(currentGame.getMatch(),newHome,
                        newAway,null,0,0,false);
                this.games.remove(i);
                this.games.add(newGame);
                String match = currentGame.getMatch();
                notifyGamesResults(match,newHome,newAway);
            }
        }
    }
}
