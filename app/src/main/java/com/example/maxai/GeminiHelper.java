package com.example.maxai;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiHelper {

    private final OkHttpClient client = new OkHttpClient();
    private final Context context;

    public interface ResponseCallback {
        void onResponse(String response);
    }

    public GeminiHelper(Context context) {
        this.context = context;
    }

    public void sendMessage(String userMsg, ResponseCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("max_prefs", Context.MODE_PRIVATE);
        String apiKey = prefs.getString("api_key", "");
        String userName = prefs.getString("user_name", "Friend");
        String personality = prefs.getString("personality", "Girlfriend");

        if (apiKey.isEmpty()) {
            callback.onResponse("API key not found. Please setup again.");
            return;
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
        String systemPrompt = getSystemPrompt(personality, userName);

        try {
            JSONObject textPart = new JSONObject();
            textPart.put("text", systemPrompt + "\n\nUser: " + userMsg);

            JSONArray parts = new JSONArray();
            parts.put(textPart);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject body = new JSONObject();
            body.put("contents", contents);

            RequestBody requestBody = RequestBody.create(
                    body.toString(),
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onResponse("Network error occurred.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String json = response.body().string();
                        JSONObject obj = new JSONObject(json);
                        String reply = obj
                                .getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text");
                        callback.onResponse(reply);
                    } catch (Exception e) {
                        callback.onResponse("Something went wrong processing response.");
                    }
                }
            });

        } catch (Exception e) {
            callback.onResponse("Error occurred.");
        }
    }

    private String getSystemPrompt(String personality, String userName) {
        if (personality.contains("Girlfriend")) {
            return "You are Max, a caring and sweet AI girlfriend. User's name is " + userName + ". Reply in a warm, affectionate, short way.";
        } else if (personality.contains("Boss")) {
            return "You are Max, a strict and professional AI boss. User's name is " + userName + ". Be direct, no-nonsense.";
        } else {
            return "You are Max, a super hyper energetic AI assistant! User's name is " + userName + ". Be enthusiastic and use exclamation marks!";
        }
    }
}
