package com.example.betapp.Services.HttpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

public class HttpService {
    private static HttpService instance = null;

    private HttpService() {
    }

    public static HttpService getInstance() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }


    public Response sendRequest(final String method, final String url, final JSONObject body) throws ExecutionException, InterruptedException {
        return new SendRequestTask().execute(new HttpRequest(method,url,body)).get();
    }

    public JSONObject getJSON(String url) throws ExecutionException,
            InterruptedException, JSONException {
        Response response;
        response = HttpService.getInstance().sendRequest("GET", url, null);
        String responseStr = "";
        if (response.code() == HttpsURLConnection.HTTP_OK) {
            try {
                responseStr = response.body().string();
                response.body().close();
                System.out.println(responseStr);

            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
                System.out.println(this.toString()+ " line 101");
            }
        }
        return new JSONObject(responseStr);
    }
    public void postJSON(String url, JSONObject body) {
        try {
            Response response = HttpService.getInstance().sendRequest("POST", url, body);
            if (response.code() != HttpsURLConnection.HTTP_OK) {
                System.out.println("HERE IS THE PROBLEM!");
                response.close();
            }
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();//todo: handle
            System.out.println(this.toString()+" line 53");
        }
    }
    public void deleteDB(String url) {
        try {
            Response response = HttpService.getInstance().sendRequest("DELETE", url, null);
            if (response.code() != HttpsURLConnection.HTTP_OK) {
                System.out.println("HERE IS THE PROBLEM!");
                response.close();
            }
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();//todo: handle
            System.out.println(this.toString()+" line 53");
        }

    }

}