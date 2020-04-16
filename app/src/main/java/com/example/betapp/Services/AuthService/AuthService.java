package com.example.betapp.Services.AuthService;

import com.example.betapp.Consts;
import com.example.betapp.Services.HttpService.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;


public class AuthService {
    private static AuthService instance = null;
    private Boolean isloggedIn;
    public static final int SIGN_IN = 0;
    public static final int SIGN_UP = 1;

    private AuthService() {
        this.isloggedIn = false;
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    // mode: 0 for sign in, 1 for sign up
    public int signInOrSignUp(String emailAddress, String password, int mode) throws ExecutionException, InterruptedException, JSONException, IOException {
        String googleApi = "";
        if (mode == SIGN_IN) {
            googleApi = Consts.SIGN_IN_API;
        } else if (mode == SIGN_UP) {
            googleApi = Consts.SIGN_UP_API;
        }
        googleApi += Consts.WEB_API_KEY;

        JSONObject credentials = new JSONObject().put("email", emailAddress)
                .put("password", password).put("returnSecureToken", "true");
        Response response = HttpService.getInstance().sendRequest("POST", googleApi, credentials);

        if (response.code() == HttpsURLConnection.HTTP_OK) {
            if (mode == SIGN_UP) {
                addUserToDataBase(emailAddress);
            } else {
                this.isloggedIn = true;
            }
            return 1;
        }
        return 0;
    }

    public Boolean getIsLoggedIn() {
        return this.isloggedIn;
    }

    public void setIsLoggedIn(boolean mode) {
        this.isloggedIn = mode;
    }

    private void addUserToDataBase(String emailAddress) throws JSONException, ExecutionException, InterruptedException, IOException {
        String usersDatabase = Consts.USERS_DATABASE_API;
        JSONObject body = new JSONObject().put(emailAddress.replace('.', ':'), new JSONObject().put(
                "name", emailAddress.split("@")[0]).put(
                        "groups", new JSONArray()
                ));
        HttpService.getInstance().sendRequest("PUT",
                usersDatabase, body);
    }
}