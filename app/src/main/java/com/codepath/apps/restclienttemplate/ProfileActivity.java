package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    ImageView ivProfileImage;
    TextView tvName;
    TextView tvScreenName;
    ImageView ivBanner;
    FollowingAdapter followingAdapter;
    FollowersAdapter followersAdapter;
    RecyclerView rvFollowing;
    RecyclerView rvFollowers;
    List<User> followingList;
    List<User> followersList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = TwitterApp.getRestClient(this);
        User user= Parcels.unwrap(getIntent().getParcelableExtra("user"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivProfileImage=findViewById(R.id.ivProfileImage);
        tvName=findViewById(R.id.tvName);
        tvScreenName=findViewById(R.id.tvScreenName);
        ivBanner=findViewById(R.id.ivBanner);
        rvFollowing=findViewById(R.id.rvFollowing);
        rvFollowers=findViewById(R.id.rvFollowers);
        this.bind(user);
        followingList=new ArrayList<>();
        followersList=new ArrayList<>();

        followingAdapter = new FollowingAdapter(this, followingList);
        followersAdapter = new FollowersAdapter(this, followersList);

        //recycler view setup :layout manager and the adapter
        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        rvFollowing.setLayoutManager(llm);
        rvFollowing.setAdapter(followingAdapter);
        rvFollowers.setLayoutManager(llm2);
        rvFollowers.setAdapter(followersAdapter);
        populateFollowing(user);
        populateFollowers(user);

    }

    public void bind(User user){
        tvName.setText(user.name);
        tvScreenName.setText("@"+user.screenName);
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }

    public void populateFollowing(User user){
        client.getFollowing(user.userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for(int i =0; i<jsonArray.length(); i++){
                        followingList.add(User.fromJson(jsonArray.getJSONObject(i)));
                        Log.i("adding", "adding:");
                    }
                    followingAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i("bad", "bad:");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("worse", "worse:");
            }
        });
    }

    public void populateFollowers(User user){
        client.getFollowers(user.userId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject user = jsonArray.getJSONObject(i);
                        followersList.add(User.fromJson(user));
                    }
                    followersAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

}