package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvScreenName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User user= Parcels.unwrap(getIntent().getParcelableExtra("user"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivProfileImage=findViewById(R.id.ivProfileImage);
        tvName=findViewById(R.id.tvName);
        tvScreenName=findViewById(R.id.tvScreenName);
        this.bind(user);
    }

    public void bind(User user){
        tvName.setText(user.name);
        tvScreenName.setText("@"+user.screenName);
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }
}