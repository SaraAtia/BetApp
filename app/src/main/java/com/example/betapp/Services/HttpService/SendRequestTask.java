package com.example.betapp.Services.HttpService;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendRequestTask extends AsyncTask<HttpRequest,Void, Response> {
    protected Response doInBackground(HttpRequest... httpRequests) {
        String method = httpRequests[0].getMethod();
        String url = httpRequests[0].getUrl();
        JSONObject body = httpRequests[0].getBody();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = null;
        if (body != null){
            requestBody = RequestBody.create(mediaType, body.toString());
        }
        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
