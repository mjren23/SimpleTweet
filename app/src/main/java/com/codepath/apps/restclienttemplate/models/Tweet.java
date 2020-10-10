package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;

    public static Tweet fromJsonObject(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJsonObject(jsonObject.getJSONObject("user"));
//        Log.i("TimelineActivity", "added tweets");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < jsonArray.length(); i++) {
//            Log.i("TimelineActivity", "adding tweet " + jsonArray.getJSONObject(i));
            tweets.add(fromJsonObject(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }
}
