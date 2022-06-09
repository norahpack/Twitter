package com.codepath.apps.restclienttemplate;

import static com.facebook.stetho.inspector.network.ResponseHandlingInputStream.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    TextView tvLikes;
    TextView tvRetweets;
    boolean liked = false;
    boolean retweeted = false;
    Drawable likeBackground;
    Drawable retweetBackground;

    private AppBarConfiguration appBarConfiguration;
    private ActivitySingleTweetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long oldTime;
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
        tvLikes=findViewById(R.id.tvLikes);
        tvRetweets=findViewById(R.id.tvRetweets);


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
        btnRetweet.setTag(tweet.getTweet_id());
        tvLikes.setText(tweet.favorite_count);
        tvRetweets.setText(tweet.retweet_count);
        liked=tweet.is_favorited;
        retweeted=tweet.is_retweeted;

        //sets like and retweet button to desired initial state
        if(liked) {
            likeBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart);
        } else {
            likeBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart_stroke);
        }

        if(retweeted){
            retweetBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_retweet);
        } else {
            retweetBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_retweet_stroke);
        }
        btnLike.setBackground(likeBackground);
        btnRetweet.setBackground(retweetBackground);

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        if(tweet.tweet_URL!="none") {
            ivTweet.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.tweet_URL).into(ivTweet);

        }
    }

    public void replyMethod(View view){
        Intent intent = new Intent(this, ComposeActivity.class);

        intent.putExtra("replyTo", view.getTag().toString());
        startActivity(intent);
    }

    public void retweetMethod(View view){
        Drawable newBackground;
        String message;

        int change_retweets;

        if (retweeted){
            newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_retweet_stroke);
            retweeted = false;
            message = "unretweet";
            change_retweets=-1;
        } else {
            newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_retweet);
            retweeted = true;
            message="retweet";
            change_retweets=1;
        }

        client=TwitterApp.getRestClient(this);
        client.retweetTweet(view.getTag().toString(), retweeted, new JsonHttpResponseHandler(){
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                btnRetweet.setBackground(newBackground);
                tvRetweets.setText((String.valueOf((Integer.valueOf((String) tvRetweets.getText()))+change_retweets)));
                Log.i(TAG, "onSuccess to " + message + " tweet");
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to unretweet/retweet tweet", throwable);
            }
        });
    }

    public void likeMethod(View view){
        Drawable newBackground;
        String message;
        int change_likes;

        if (liked){
             newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart_stroke);
             liked = false;
             message = "unlike";
             change_likes=-1;
        } else {
             newBackground = AppCompatResources.getDrawable(SingleTweetActivity.this, R.drawable.ic_vector_heart);
             liked = true;
             message="like";
             change_likes=1;
        }

        client=TwitterApp.getRestClient(this);
        client.likeTweet(view.getTag().toString(), liked, new JsonHttpResponseHandler(){
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                btnLike.setBackground(newBackground);
                System.out.println((String)tvLikes.getText());
                tvLikes.setText((String.valueOf((Integer.valueOf((String) tvLikes.getText()))+change_likes)));
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