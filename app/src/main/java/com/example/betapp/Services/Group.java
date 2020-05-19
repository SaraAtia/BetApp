package com.example.betapp.Services;


import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
//TODO: remove group from database

public class Group {
//    private HashMap<String, ArrayList<String>> group;
    public String groupID;
    public String groupName;
    public ArrayList<String> users; // string = userID
    public boolean status; // is the group active
    public HashMap<String, String> games; // gameID: name
    private HashMap<String, ArrayList<String>> games_by_date;//Date:gamesID
    private HashMap<String, Integer> users_rank;//userID:rank

    public Group(String id){
        this.groupName = "";
        this.groupID = id;
        this.games_by_date = new HashMap<>();
        this.games = new HashMap<>();
        this.status = false;
        this.users_rank = new HashMap<>();
        this.users = new ArrayList<>();
    }

    public Group(){}
    public String getGroupID() {
        return groupID;
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
        return null; //TODO
    }

    public static Group createGroupOnDB(){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String groupID = DB.getReference("groups").push().getKey();
        Group group = new Group(groupID);
        DB.getReference("groups").child(groupID).setValue(group);
        return group;
    }

    public void setGroupStatus(boolean newState) {
        this.status = newState;
        // update status on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.groupID).child("status").setValue(newState);
    }

    public void setGroupName(String group_name) {
        this.groupName = group_name;
        // update name on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.groupID).
                child("groupName").setValue(group_name);
    }

    public void setGroupGames(HashMap<String, String> games) { //todo:
        this.games = games;
        // update name on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        for(String gameID: games.keySet()){
            DB.getReference("groups").child(this.groupID).
                child("games").child(gameID).setValue(games.get(gameID));
        }
    }

    public void addUser(String userID){
        this.users.add(userID);
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("groups").child(this.groupID).
                child("users").setValue(this.users);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}