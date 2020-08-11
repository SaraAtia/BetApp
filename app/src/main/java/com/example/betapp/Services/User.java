package com.example.betapp.Services;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
public class User {
    public String userID;
    public String user_name; //TODO: ask for name in authentication
    public HashMap<String, String> m_groups = new HashMap<>(); // groupName, groupID


    public User(){}
    public User(String user_id, String user_name){
        this.userID = user_id;
        this.user_name = user_name;
    }

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
        return userID;
    }

    /**
     * add group to user info on DB.
     * @param groupName
     * @param groupID
     */
    public void addGroup(String groupName, String groupID) {
        this.m_groups.put(groupName, groupID);
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DB.getReference("users").child(this.userID).
                child("m_groups").setValue(m_groups);
    }
    /**
     * Create a new user on DB.
     * @return
     */
    public static User createUser(String user_name) {
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry =  DB.getReference("users").push().getKey();
        User user = new User(entry, user_name);
        DB.getReference("users").child(entry).setValue(user);/*.setValue(user_name);
        DB.getReference("users").child(entry).child("user_id").setValue(entry);*/
        return user;
    }
}
