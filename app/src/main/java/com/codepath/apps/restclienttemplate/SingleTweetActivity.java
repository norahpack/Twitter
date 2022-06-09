package com.codepath.apps.restclienttemplate;

import static com.facebook.stetho.inspector.network.ResponseHandlingInputStream.TAG;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.codepath.apps.restclienttemplate.databinding.ActivitySingleTweetBinding;

import org.parceler.Parcels;

import okhttp3.Headers;

public class SingleTweetActivity extends AppCompatActivity {

    TwitterClient client;
    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    ImageView ivTweet;
    TextView tvName;
    TextView tvTime;
    Button btnReply;
    Button btnRetweet;
    Button btnLike;
    boolean liked = false;

    private AppBarConfiguration appBarConfiguration;
    private ActivitySingleTweetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_tweet);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody=findViewById(R.id.tvBody);
        tvScreenName=findViewById(R.id.tvScreenName);
        ivTweet=findViewById(R.id.ivTweet);
        tvName=findViewById(R.id.tvName);
        tvTime=findViewById(R.id.tvTime);
        btnReply=findViewById(R.id.btnReply);
        btnLike=findViewById(R.id.btnLike);
        btnRetweet=findViewById(R.id.btnRetweet);
        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        this.bind(tweet);
    }

    public void bind(Tweet tweet) {
        tvBody.setText(tweet.body);
        tvScreenName.setText("@"+tweet.user.screenName);
        tvName.setText(tweet.user.name);
        tvTime.setText(tweet.relative_time);
        btnReply.setTag("@"+tweet.user.screenName);
        btnLike.setTag(tweet.getTweet_id());
        btnRetweet.setTag("@"+tweet.user.screenName);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        if(tweet.tweet_URL!="none") {
            ivTweet.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.tweet_URL).into(ivTweet);

        }
    }

    public void likeMethod(View view){
        Drawable newBackground;
        String message;


        if (liked == true){
             newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart_stroke);
             liked = false;
             message = "unlike";
        } else {
             newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart);
             liked = true;
             message="like";
        }

        client=TwitterApp.getRestClient(this);
        client.likeTweet(view.getTag().toString(), liked, new JsonHttpResponseHandler(){
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                btnLike.setBackground(newBackground);
                Log.i(TAG, "onSuccess to " + message + " tweet");
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to like/unlike tweet", throwable);
            }
        });
    }
}