package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.betapp.Services.Group;
import com.example.betapp.Services.NotificationService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * My Groups activity allow to create a group or join to exist group by a code.
 * The activity contains all group groups user participate in.
 */
public class MyGroups extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ArrayList<Group> user_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        LinearLayout l = findViewById(R.id.my_groups_layout);
        mAuth = FirebaseAuth.getInstance();
        // user ID as saved in authentication info
        String firebaseUserID = getIntent().getStringExtra("userID");
        //Todo: read from DB all user groups and set button
        // for each group with the name of the group
        ArrayList<String> groups_str = new ArrayList<>(); // Todo: add name of groups to array

        //TODO: pull user info by user id and create new user.
        // get user's groups and create button for each.
        // Each button has on click func (openGroup).
//        User curr_user = users_DB.child(user_entry).child("m_groups");
        Button b = new Button(this);
        // TODO: try to pull info from DB and update it.
        /*users_DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        users_DB.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 String name = (String) dataSnapshot.child("checked").getValue();
                 String date = (String) dataSnapshot.child("desc").getValue();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
        });*/
//        String dataID = mRefUsers.push().getKey();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("Group6 vs Group 5", "kjhigkjh");
//        User sara = new User(dataID, "USER2", map);
//        mRefUsers.child(dataID).setValue(sara);
//        HashMap<String, String> mapp = new HashMap<>();
//        mapp.put(firebaseUserID, dataID);
//        myRef.child(map_key).setValue(mapp);
//        myRef.child(map_key).
//        String dataID = myRef.push().getKey();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("Group6 vs Group 5", "kjhigkjh");
//        User sara = new User(dataID, "dekel", map);
//        myRef.child(dataID).setValue(sara);
//        System.out.println();
//        myRef.child("-M5b7nY3r7zXZvahG-OQ").removeValue();
    /*  for(int i =0; i <7; i++){
            Button btnShow = new Button(this);
            btnShow.setText(groupsStr.get(i));
            btnShow.setLayoutParams(new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Works!");
                }
            });
            l.addView(btnShow);
        }*/
        this.startService(); // TODO: uncomment to start notification service
    }

    public void openGroup(View view){
        String groupID = view.getTag().toString();
        Intent intent = new Intent(this, Gamble.class);//TODO: open group's screen
        intent.putExtra("groupID", groupID);
        startActivity(intent);
    }

    public void createGroup(View view){
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }
    public void joinGroup(View view) {
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }

    public void signOut(View view){
        mAuth.signOut();
        startActivity(new Intent(this, AuthActivity.class));
    }

    public void startService(){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

}
