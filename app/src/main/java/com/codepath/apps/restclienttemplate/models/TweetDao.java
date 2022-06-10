package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.user_id AS tweet_user_id, Tweet.tweet_id AS tweet_tweet_id, Tweet.retweet_count as tweet_retweet_count, Tweet.favorite_count AS tweet_favorite_count, Tweet.is_favorited AS tweet_is_favorited, Tweet.is_retweeted AS tweet_is_retweeted, Tweet.relative_time as tweet_relative_time, Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id, Tweet.tweet_URL AS tweet_tweet_URL,  User.*" + " FROM tweet INNER JOIN User ON Tweet.user_id = User.id ORDER BY createdAt DESC LIMIT 300")
    List<TweetWithUser> recentItems();

    //ellipses in the method arguments mean the method can take in a variable number of arguments.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
