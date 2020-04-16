package com.example.betapp.Services.HttpService;
import org.json.JSONObject;

public class HttpRequest {
    private String method;
    private String url;
    private JSONObject body;

    public HttpRequest(String method, String url, JSONObject body){
        this.method = method;
        this.url=url;
        this.body=body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public JSONObject getBody() {
        return body;
    }
}
