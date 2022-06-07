package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH=140;
    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose=findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);

        //set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent=etCompose.getText().toString();
                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length()>MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
                //make an api call to twitter to publish the tweet

            }
        });


    }
}