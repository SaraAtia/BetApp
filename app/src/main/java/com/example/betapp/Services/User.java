package com.example.betapp.Services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
//TODO: remove user from database
public class User {
    public String user_ID;
    public String user_name; //TODO: ask for name in authentication
    public HashMap<String, String> m_groups = new HashMap<>(); // groupName, groupID


    public HashMap<String, String> getUserGroups() {
        return m_groups;
    }

    public String getUserID() {
        return user_ID;
    }

    public void addGroup(String groupName, String groupID) {
        this.m_groups.put(groupName, groupID);
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("users").child(this.user_ID).
                child("m_groups").setValue(m_groups);
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public void setGroups(HashMap<String, String> m_groups) {
        this.m_groups = m_groups;
    }

    public String getUserName() {
        return user_name;
    }
}
