package com.codepath.apps.restclienttemplate.models;

import static com.facebook.stetho.inspector.network.ResponseHandlingInputStream.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.ComposeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity= User.class, parentColumns="id", childColumns="user_id"))
public class Tweet {

    @PrimaryKey
    @ColumnInfo
    public long id;

    @ColumnInfo
    public String body;

    @ColumnInfo
    public String createdAt;

    @Ignore
    public User user;

    @ColumnInfo
    public String tweet_URL;

    @ColumnInfo
    public String relative_time;

    @ColumnInfo
    public long user_id;

    @ColumnInfo
    public String tweet_id;

    @ColumnInfo
    public String retweet_count;

    @ColumnInfo
    public String favorite_count;

    @ColumnInfo
    public boolean is_favorited;

    @ColumnInfo
    public boolean is_retweeted;

    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt=jsonObject.getString("created_at");
        tweet.favorite_count=jsonObject.getString("favorite_count");
        tweet.retweet_count=jsonObject.getString("retweet_count");
        tweet.is_favorited=jsonObject.getBoolean("favorited");
        tweet.is_retweeted=jsonObject.getBoolean("retweeted");
        tweet.tweet_id=jsonObject.getString("id_str");
        tweet.id=jsonObject.getLong("id");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user=user;
        tweet.user_id=user.id;
        if(jsonObject.getJSONObject("entities").has("media")){
            Log.d("Tweet", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"));
            tweet.tweet_URL=jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        } else {
            Log.d("Tweet", "No pictures");
            tweet.tweet_URL="none";
        }
        tweet.relative_time=tweet.getRelativeTimeAgo(tweet.createdAt);
        return tweet;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    @SuppressLint("LongLogTag")
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }


    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRelative_time() {
        return relative_time;
    }

    public String getTweet_id() {
        return tweet_id;
    }

    public String getTweet_URL() {
        return tweet_URL;
    }
}
