package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.betapp.Services.Group;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.betapp.Services.NotificationService.PopupMsg;

public class AllGamesPresentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_games_presentation);
        final LinearLayout layout = findViewById(R.id.all_games_layout);
        final String groupID = getIntent().getStringExtra("groupID");
        final Context context = this;
        DatabaseReference groups_DB = FirebaseDatabase.getInstance().getReference("groups");
        groups_DB.addValueEventListener(new ValueEventListener(){
            /**
             * show all games in group - each game presented as button.
             * @param dataSnapshot DB data
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group curr_group = dataSnapshot.child(groupID).getValue(Group.class);
                if (curr_group == null) {
                    // unexpected error uploading data from DB
                    PopupMsg(context, "Error Occurred - Couldn't Find Group Info",
                            Toast.LENGTH_SHORT);
                } else {
                    HashMap<String, String> games = curr_group.games; // id:name
                    if (games != null) {
                        Object[] games_ids = games.keySet().toArray();
                        Object[] games_names = games.values().toArray();
                        for (int i = 0; i < games.size(); i++) {
                            Button btnShow = new Button(context);
                            btnShow.setText((String) games_names[i]);
                            btnShow.setLayoutParams(new LinearLayout.LayoutParams
                                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            btnShow.setTag((String) games_ids[i]);
                            btnShow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openGamePresentation(v.getTag().toString());
                                }
                            });
                            layout.addView(btnShow);
                        }
                    } else {
                        TextView no_game_msg_view = new TextView(context);
                        no_game_msg_view.setText("No Games To Present For Now");
                        no_game_msg_view.setTextColor(Color.parseColor("#FFFFFF"));
                        no_game_msg_view.setTextSize(18);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                        no_game_msg_view.setLayoutParams(layoutParams);
                        /*no_game_msg_view.setLayoutParams(new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,));*/
                        layout.addView(no_game_msg_view);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Open game presentation of game btn clicked.
     * @param gameID
     */
     private void openGamePresentation(String gameID){
         Intent intent = new Intent(this, GamePresentation.class);
         intent.putExtra("gameID", gameID);
         startActivity(intent);
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
