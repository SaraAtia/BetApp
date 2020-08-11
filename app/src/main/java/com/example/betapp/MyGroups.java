package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.betapp.Services.Group;
import com.example.betapp.Services.GroupPresentation;
import com.example.betapp.Services.NotificationService;
import com.example.betapp.Services.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * My Groups activity allow to create a group or join to exist group by a code.
 * The activity contains all group groups user participate in.
 */
public class MyGroups extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public String mUserID;
    public static User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        final LinearLayout layout = findViewById(R.id.my_groups_layout);
        mAuth = FirebaseAuth.getInstance();
        // user ID as saved in authentication info
        final String userIDAuth = getIntent().getStringExtra("userIDAuth");
        final String userName = "Dekel"; //getIntent().getStringExtra("userName");//todo
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Context context = this;
        if(userIDAuth!=null){
            // get the map between user id in authentication to user entry in user's database
            final DatabaseReference user_map_DB = database.getReference("FBUidToDBUid");
            user_map_DB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(userIDAuth).exists()){
                        mUser = User.createUser(userName);
                        user_map_DB.child(userIDAuth).setValue(mUser.userID);
                        // get the map between user id in authentication to user entry in user's database
                        AuthActivity.mUser = mUser;
                    }
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final String mUserID;
                        if (!ds.getKey().equals(userIDAuth)) { //other child's - irrelevant
                            continue;
                        }
                        mUserID = ds.getValue(String.class);
                        final DatabaseReference users_DB = database.getReference("users");
                        users_DB.addValueEventListener(new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mUserID == null) { //other child's - irrelevant
                                    return;
                                }
                                mUser = dataSnapshot.child(mUserID).getValue(User.class);
                                AuthActivity.mUser = mUser;
                                HashMap<String, String> groups = mUser.getUserGroups();
                                if (groups != null) {
                                    Object[] groups_names = groups.keySet().toArray();
                                    Object[] groups_ids = groups.values().toArray();
                                    for (int i = 0; i < groups.size(); i++) {
                                        Button btnShow = new Button(context);
                                        btnShow.setText((String) groups_names[i]);
                                        btnShow.setLayoutParams(new LinearLayout.LayoutParams
                                                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.
                                                        LayoutParams.WRAP_CONTENT));
                                        btnShow.setTag((String) groups_ids[i]);
                                        btnShow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openGroup(v.getTag().toString());
                                            }
                                        });
                                        layout.addView(btnShow);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            DatabaseReference users_DB = database.getReference("users");
            users_DB.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.child(AuthActivity.mUser.getUserID()).getValue(User.class);
                    HashMap<String, String> groups = mUser.getUserGroups();
                    if (groups != null) {
                        Object[] groups_names = groups.keySet().toArray();
                        Object[] groups_ids = groups.values().toArray();
                        for (int i = 0; i < groups.size(); i++) {
                            Button btnShow = new Button(context);
                            btnShow.setText((String) groups_names[i]);
                            btnShow.setLayoutParams(new LinearLayout.LayoutParams
                                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.
                                            LayoutParams.WRAP_CONTENT));
                            btnShow.setTag((String) groups_ids[i]);
                            btnShow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openGroup(v.getTag().toString());
                                }
                            });
                            layout.addView(btnShow);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        this.startService(); // TODO: uncomment to start notification service
    }

    private void openGroup(String groupID){
        Intent intent = new Intent(this, GroupPresentation.class);
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
