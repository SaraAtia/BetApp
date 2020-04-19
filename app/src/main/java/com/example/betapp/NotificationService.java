package com.example.betapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDate;
import java.util.Date;

import static com.example.betapp.App.CHANNEL_ID;

public class NotificationService extends IntentService {

    public NotificationService( )
    {
        super( "NotificationService" );
    }



    public NotificationService(String name )
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
            notifyGamesResults();
        }
    }

    private void notifyGamesResults(){
        Intent notificationIntent = new Intent(this, AuthActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TITLE")
                .setContentText("content")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.notify(1,notification);
        }
    }
}
