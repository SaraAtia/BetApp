package com.example.betapp;

public final class Consts {
    public static final String USERS_DATABASE_API = "https://bet-app-78253.firebaseio.com/users.json";
    public static final String GROUPS_DATABASE = "https://bet-app-78253.firebaseio.com/groups.json";
    public static final String WEB_API_KEY = "AIzaSyAV59eKoRLyhS2S_ChjOmueuFu9If7c-Pw";
    public static final String SIGN_IN_API = "https://identitytoolkit.googleapis.com/v1/" +
            "accounts:signInWithPassword?key=";
    public static final String SIGN_UP_API = "https://identitytoolkit.googleapis.com/v1/" +
            "accounts:signUp?key=";
    public static final String NEXT15EVENTS_BY_LEAGUEID = "https://www.thesportsdb.com/api" +
            "/v1/json/4013017/eventsnextleague.php?id=";
    public static final String PLAYERS_BY_TEAM_ID = "https://www.thesportsdb.com/api/v1/" +
            "json/4013017/lookup_all_players.php?id=";
    public static final int MAX_GAMES_TO_BET = 10;
    public static final String LEAGUES_FILE_NAME = "Leagues.txt";

    private Consts(){}
}