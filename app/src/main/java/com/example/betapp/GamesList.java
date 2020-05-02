package com.example.betapp;

import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

import static com.example.betapp.Consts.GROUPS_DATABASE;

public class GamesList {
    private int size;
    private JSONObject eventsInfo;
    private ArrayList<HashMap<String,String>> games;

    public GamesList(int size){
        this.eventsInfo = new JSONObject();
        this.size = size;
        this.games = new ArrayList<>();
    }

    public int getSize(){
        return this.size;
    }

    public JSONObject getGames() {
        return eventsInfo;
    }

    public LinkedHashMap<String, String> getGame(int position){
        try {
            return (LinkedHashMap<String, String>) eventsInfo.get(String.valueOf(position));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addGame(String s, LinkedHashMap<String,String> game) throws JSONException {
        //TODO: check length is the amount of events
        if(eventsInfo.length() < this.size){
            this.eventsInfo.put(s, game);
            return true;
        } else{
            return false;
        }
    }

    public void uploadToDB() {
        //TODO: generate group's code and add it to json
        //HttpService.getInstance().postJSON(GROUPS_DATABASE, this.eventsInfo);
    }
}
