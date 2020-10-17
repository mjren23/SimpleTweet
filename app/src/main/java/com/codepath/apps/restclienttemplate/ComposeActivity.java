package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;

    EditText editTweet;
    Button btnTweet;
    TextView tvCounter;

    TwitterClient client;

    int numChars;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        editTweet = findViewById(R.id.editTweet);
        btnTweet = findViewById(R.id.btnTweet);
        tvCounter = findViewById(R.id.tvCounter);

        client = TwitterApp.getRestClient(this);

        numChars = 0;
        tvCounter.setText(numChars+"/"+MAX_TWEET_LENGTH);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = editTweet.getText().toString();

                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > 280) {
                    Toast.makeText(ComposeActivity.this, "Tweet is too long!", Toast.LENGTH_LONG).show();
                    return;
                }

                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJsonObject(json.jsonObject);
                            Log.i("ComposeActivity", "published tweet: " + tweet.body);
                            Intent result = new Intent();
                            result.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, result);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });


            }
        });

        editTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCounter.setText(numChars+"/"+MAX_TWEET_LENGTH);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numChars = charSequence.length();
                tvCounter.setText(numChars+"/"+MAX_TWEET_LENGTH);
                if (numChars > MAX_TWEET_LENGTH) {
                    tvCounter.setTextColor(Color.RED);
                }
                else {
                    tvCounter.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCounter.setText(numChars+"/"+MAX_TWEET_LENGTH);
            }
        });
    }
}