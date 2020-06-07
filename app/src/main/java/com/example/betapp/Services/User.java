package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
//TODO: remove user from database
public class User {
    public String user_ID;
    public String user_name; //TODO: ask for name in authentication
    public HashMap<String, String> m_groups = new HashMap<>(); // groupName, groupID


    /**
     * getter.
     * @return
     */
    public HashMap<String, String> getUserGroups() {
        return m_groups;
    }

    /**
     * getter.
     * @return
     */
    public String getUserID() {
        return user_ID;
    }

    /**
     * add group to user info on DB.
     * @param groupName
     * @param groupID
     */
    public void addGroup(String groupName, String groupID) {
        this.m_groups.put(groupName, groupID);
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("users").child(this.user_ID).
                child("m_groups").setValue(m_groups);
    }
}
