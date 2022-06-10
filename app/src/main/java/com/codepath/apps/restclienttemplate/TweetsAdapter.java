package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    //CHNAGE THIS LATER TO OTHER WAY ANDREW SAID
    Context context;
    List<Tweet> tweets = new ArrayList<>();
    //pass in context and list of tweets


    @NonNull
    @Override
    //for each row inflate a layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view) ;
    }
    //bind values based on the position

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data
        Tweet tweet = tweets.get(position);
        // then bind the data at the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }




    //define a view holder(Star here when coding)
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTwitterImage;


        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTwitterImage = itemView.findViewById(R.id.ivTwitterImage);


        }

        public void bind(Tweet tweet) {
            Log.i("Here", tweet.body);
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(ivProfileImage.getContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

            if (tweet.bodyImage.equals("null")) {
                ivTwitterImage.setVisibility(View.GONE);
            } else {
                ivTwitterImage.setVisibility(View.VISIBLE);
                Glide.with(ivTwitterImage.getContext()).load(tweet.bodyImage).into(ivTwitterImage);
            }

            //Glide.with(ivTwitterImage.getContext()).load(tweet.bodyImage).into(ivTwitterImage);



        }




    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public void addToTop(Tweet tweet){
        tweets.add(0,tweet);
    }
}
