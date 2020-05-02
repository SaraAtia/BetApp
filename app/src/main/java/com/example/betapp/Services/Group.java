package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//TODO: remove group from database

public class Group {
//    private HashMap<String, ArrayList<String>> group;
    private boolean m_state;
    private String m_groupID;
    private String m_group_name;
    private ArrayList<String> users; // string = userID
    private HashMap<Date, ArrayList<String>> m_games_by_date;//Date:gamesID
    private HashMap<String, Game> m_games;
    private HashMap<String, Integer> m_users_rank;//userID:rank


    public Group(String id){
        this.m_group_name = "";
        this.m_groupID = id;
        this.m_games_by_date = new HashMap<>();
        this.m_games = new HashMap<>();
        this.m_users_rank = new HashMap<>();
        this.users = new ArrayList<>();
    }

    public String getGroupID() {
        return m_groupID;
    }

    public void uploadToDB(){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String key = DB.getReference("groups").push().getKey();
        DB.getReference().child(key).setValue(this);
    }

    /**
     * Pull group from DB by groupID.
     * @param groupId - string
     */
    public static Group getGroup(String groupId){
        return null; //TODO: pull from DB
//        return new Group();
    }

    public ArrayList<Game> getGames(){
        return new ArrayList<>(this.m_games.values());
    }

    public static Group createGroupOnDB(){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String groupID = DB.getReference("groups").push().getKey();
        return new Group(groupID);
    }

    public void setGroupState(boolean newState) {
        this.m_state = newState;
        // update status on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.m_groupID).child("status").setValue(newState);
    }

    public void setGroupName(String group_name) {
        this.m_group_name = group_name;
        // update name on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.m_groupID).
                child("group_name").setValue(group_name);
    }

    public void setGroupGames(HashMap<String, Game> games) {
        this.m_games = games;
        // TODO: upload to DB?
    }

    public void addUser(String userID){
        this.users.add(userID);
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.m_groupID).
                child("users").setValue(userID);
    }

    public String getGroupName() {
        return m_group_name;
    }
}