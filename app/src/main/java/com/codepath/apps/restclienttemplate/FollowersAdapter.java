package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    Context context;
    List<User> users;



    //pass in the context and list of tweets
    public FollowersAdapter(Context context, List<User> users){
        this.context=context;
        this.users=users;
    }

    @NonNull
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new FollowersAdapter.ViewHolder(view);
    }

    //bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.ViewHolder holder, int position) {
        //get the data at position
        User user = users.get(position);

        //bind the tweet with the viewholder
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //define a  viewHolder

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ItemTweetBinding itemTweetBinding;

        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
            //this.itemTweetBinding=itemView;

        }

        public void bind(User user) {
            tvScreenName.setText("@" + user.screenName);
            tvName.setText(user.name);
            tvScreenName.setTag(user);
            Glide.with(context).load(user.profileImageUrl).into(ivProfileImage);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User user = users.get(position);
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                context.startActivity(intent);
            }
        }
    }
}
