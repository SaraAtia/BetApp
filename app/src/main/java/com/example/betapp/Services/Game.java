package com.example.betapp.Services;

import com.example.betapp.Consts;
import com.example.betapp.Services.HttpService.HttpService;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


//TODO: remove game from database
public class Game {
    public String mGroupID;
    public String mGameID;
    public String mDate;
    public String mGameID_API;
    public String mGame_name;
    public String home_teamID, away_teamID;
    public boolean mAvailable_to_bet;
    public HashMap<String, String> mUsers_bets; //user id, bet id
    public HashMap<String, HashMap<String, String>> ranking_table;

    /**
     * default constructor.
     */
    public Game(){}

    /**
     * constructor.
     * @param groupID
     * @param gameID
     * @param date
     * @param gameID_API
     * @param gameName
     * @param available_to_bet
     */
    public Game(String groupID, String gameID, String date,String gameID_API, String gameName, boolean available_to_bet){
        this.mGroupID = groupID;
        this.mGameID = gameID;
        this.mGameID_API = gameID_API;
        this.mDate = date;
        this.mGame_name = gameName;
        this.mUsers_bets = new HashMap<>();
        this.mAvailable_to_bet = available_to_bet;
        this.ranking_table = new HashMap<>();
    }

    /**
     * constructor.
     * @param groupID
     * @param date
     * @param gameID
     * @param gameName
     * @param home_teamId
     * @param away_teamId
     */
    public Game(String groupID, String date, String gameID, String gameName, String home_teamId, String away_teamId) {
        this.mGroupID = groupID;
        this.mDate = date;
        this.mGameID_API = gameID;
        this.mGame_name = gameName;
        this.home_teamID = home_teamId;
        this.away_teamID = away_teamId;
        this.mUsers_bets = new HashMap<>();
        this.mAvailable_to_bet = true;
        this.ranking_table = new HashMap<>();
    }

    /**
     * setter.
     * @param game_entry
     */
    public void setGameID(String game_entry) {
        this.mGameID = game_entry;
    }

    /**
     * get the game details from the API by the game id in the API.
     * @param gameID_API
     * @return
     */
    public static HashMap<String, String> getGameDetails(String gameID_API){
        HashMap<String, String> game_details = new HashMap<>();
        try {
            //TODO: does game info can be null (if game is over) ?
            JSONObject game_info =(JSONObject) HttpService.getInstance().
                    getJSON(Consts.GAME_DETAILS_BY_EVENT_ID+gameID_API).get("events");
            game_details.put("idEvent",game_info.getString("idEvent"));
            game_details.put("strEvent",game_info.getString("strEvent"));
            game_details.put("idLeague",game_info.getString("idLeague"));
            game_details.put("strLeague",game_info.getString("strLeague"));
            game_details.put("strHomeTeam",game_info.getString("strHomeTeam"));
            game_details.put("strAwayTeam",game_info.getString("strAwayTeam"));
            game_details.put("dateEvent",game_info.getString("dateEvent"));
            game_details.put("strTimeLocal",game_info.getString("strTimeLocal"));
            return game_details;
        } catch (InterruptedException | ExecutionException| JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * upload game to DB.
     * @param game
     * @return
     */
    public static String uploadToDB(Game game){
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String entry = DB.getReference("games").push().getKey();
        game.mGameID = entry;
        DB.getReference("games").child(entry).setValue(game);
        return entry;
    }

    /**
     * create game object from the data in the DB by the game id.
     * @param gameID
     * @return
     */
    public static Game getGame(String gameID) {
        try{
            JSONObject gamesJSON = HttpService.getInstance().getJSON(Consts.GAMES_DATABASE);
            JSONObject gameJSON = (JSONObject) gamesJSON.get(gameID);
            Game game = new Game((String) gameJSON.get("mGroupID"), (String)gameJSON.get("mGameID"),
                    (String)gameJSON.get("mDate"), (String)gameJSON.get("mGameID_API"),
                    (String)gameJSON.get("mGame_name"), (boolean)gameJSON.get("mAvailable_to_bet"));
            return game;

        } catch (InterruptedException | ExecutionException| JSONException e){
            e.printStackTrace();
            return null; //TODO: to handle right
        }
    }
}
