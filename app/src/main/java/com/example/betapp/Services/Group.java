package com.example.betapp.Services;


import com.example.betapp.Consts.Error_FLAG;
import com.example.betapp.Services.HttpService.HttpService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.betapp.Consts.GROUPS_DATABASE;
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

    public Group(){
        this.groupName = "";
    }
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
     * @param groupId - string or null
     */
   /* public static Group getGroup(String groupId){
        try{
            JSONObject gameJSON = HttpService.getInstance().getJSON(Consts.GROUPS_DATABASE);
            Object group =  gameJSON.get(groupId);
            return (Group) group;

        } catch (InterruptedException | ExecutionException | JSONException e){
            return null; //TODO: to handle right
        }
        //return null; //TODO: pull from DB
//        return new Group();
    }*/

    /**
     * check if user is
     * @return
     */
   public static Error_FLAG isUserInGroup(String groupID, String userID){
       try {
           JSONObject usersJSON = ((JSONObject) HttpService.getInstance().
                   getJSON(GROUPS_DATABASE).get(groupID));
           JSONArray usersArray = (JSONArray) usersJSON.get("users");
           ArrayList<String> users_list = convert_JSONArray_to_ArrayList(usersArray);
           if(users_list.contains(userID)){
               return Error_FLAG.ALREADY_IN_GROUP;
           }
       } catch (ExecutionException|InterruptedException|JSONException e) {
           e.printStackTrace();
           return Error_FLAG.WRONG_CODE;
       }
       return Error_FLAG.NO_ERROR;
   }


   private static ArrayList<String> convert_JSONArray_to_ArrayList(JSONArray jsonArray) throws JSONException {
       ArrayList<String> arrayList = new ArrayList<String>();
       for(int i = 0; i < jsonArray.length(); i++){
           arrayList.add(jsonArray.getString(i));
       }
       return arrayList;
   }


   public static Error_FLAG addUser(String groupID, String userID){
        Error_FLAG response = isUserInGroup(groupID, userID);
        if(response == Error_FLAG.NO_ERROR){
            FirebaseDatabase DB = FirebaseDatabase.getInstance();
            JSONArray usersJSON = null;
            try {
                usersJSON =(JSONArray)((JSONObject) HttpService.getInstance().getJSON(GROUPS_DATABASE).get(groupID)).get("users");
                ArrayList<String> users = convert_JSONArray_to_ArrayList(usersJSON);
                users.add(userID);
                DB.getReference("groups").child(groupID).child("users").setValue(users);
                return Error_FLAG.NO_ERROR;
            } catch (ExecutionException|InterruptedException|JSONException e) {
                e.printStackTrace();
                return Error_FLAG.WRONG_CODE;
            }
        }
        return response;
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

    public void updateGroupName(String group_name) {
        this.groupName = group_name;
        // update name on DB
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference curr_group = DB.getReference("groups").child(this.groupID);
        DatabaseReference group_name_field = curr_group.child("groupName");
        group_name_field.setValue(group_name);
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