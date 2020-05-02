package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
//TODO: remove user from database
public class User {
    private String user_ID;
    private String user_name; //TODO: ask for name in authentication
    private HashMap<String, String> m_groups; // groupName, groupID
    public User(String id, String name, HashMap<String, String> groups){
        user_ID = id;
        user_name = name;
        m_groups = groups;
    }

    public HashMap<String, String> getUserGroups(){
        return m_groups;
    }

    public String getUserID(){
        return user_ID;
    }
    public static User getUser(String userID){
        //TODO: pull user from DB
        return null;
    }

    public void addGroup(Group group) {
        this.m_groups.put(group.getGroupName(), group.getGroupID());
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("users").child(this.user_ID).
                child("m_groups").setValue(m_groups);
    }
}
