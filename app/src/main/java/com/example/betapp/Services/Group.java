package com.example.betapp.Services;

import java.util.LinkedList;

public class Group {
    private LinkedList<UserBets> group;
    private int id;
    public Group(int id){
        this.group = new LinkedList<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LinkedList<UserBets> getGroup() {
        return group;
    }

    public void addUserToGroup(UserBets user){
        this.group.add(user);
    }

    public UserBets getUserByID(String id){
        for(UserBets userBets: this.group){
            if(userBets.getId() == id){
                return userBets;
            }
        }
        return null;
    }
}
