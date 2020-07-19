package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.betapp.Services.GroupPresentation;
import com.example.betapp.Services.HttpService.HttpRequest;
import com.example.betapp.Services.HttpService.HttpService;
import com.example.betapp.Services.NotificationService;
import com.example.betapp.Services.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * My Groups activity allow to create a group or join to exist group by a code.
 * The activity contains all group groups user participate in.
 */
public class MyGroups extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        final LinearLayout layout = findViewById(R.id.my_groups_layout);
        mAuth = FirebaseAuth.getInstance();
        // user ID as saved in authentication info
        final String userIDAuth = getIntent().getStringExtra("userIDAuth");
//        final String userName = getIntent().getStringExtra("user_name");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Context context = this;
        if(IsFirstUsage(userIDAuth)){
            this.startService();
            return;
        }
        if(AuthActivity.mUser==null) {
            //can be a new user or a signing in of old user
// get the map between user id in authentication to user entry in user's database
            final DatabaseReference user_map_DB = database.getReference("FBUidToDBUid");
            user_map_DB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final String userID = dataSnapshot.child(userIDAuth).getValue(String.class);
                        // get the map between user id in authentication to user entry in user's database
                        if (userID == null) {
                            // new user signed in
                            addUserToDB();
                            user_map_DB.child(userIDAuth).setValue(AuthActivity.mUser.userID);
                        } else {
                            DatabaseReference users_DB = database.getReference("users");
                            users_DB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        AuthActivity.mUser = ds.child(userID).getValue(User.class);
                                        HashMap<String, String> groups = AuthActivity.mUser.getUserGroups();
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
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
//                    }
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
                    AuthActivity.mUser = dataSnapshot.child(AuthActivity.mUser.getUserID()).getValue(User.class);
                    HashMap<String, String> groups = AuthActivity.mUser.getUserGroups();
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

    private boolean IsFirstUsage(String userIDAuth) {
        try {
            JSONObject users_mapJSON = HttpService.getInstance().getJSON(Consts.USERS_MAP_DB);
            return false;
        } catch (ExecutionException|InterruptedException e){
            e.printStackTrace();
            return false; //todo: handle error correctly
        }
        catch (JSONException e) {
            //if it's the first user on DB - open tables
            addUserToDB();
            HashMap<String, String> users_map = new HashMap<>(); // userIDAuth:userID_DB
            users_map.put(userIDAuth, AuthActivity.mUser.userID);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("FBUidToDBUid").setValue(users_map);
            return true;
        }
    }

    private void openGroup(String groupID){
        Intent intent = new Intent(this, GroupPresentation.class);
        intent.putExtra("groupID", groupID);
        startActivity(intent);
    }

    private void addUserToDB(){
        // new user signed in
        AuthActivity.mUser = new User();
        AuthActivity.mUser.user_name = getIntent().getStringExtra("user_name");
        User.uploadToDB(AuthActivity.mUser);
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
