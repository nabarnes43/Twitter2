package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        TextView tvTimeStamp;
        ImageButton ibFavorite;
        TextView tvFavoriteCount;


        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTwitterImage = itemView.findViewById(R.id.ivTwitterImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);


        }

        public void bind(Tweet tweet) {
            Log.i("Here", tweet.body);
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTimeStamp.setText(tweet.time);
            Glide.with(ivProfileImage.getContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

            if (tweet.bodyImage.equals("null")) {
                ivTwitterImage.setVisibility(View.GONE);
            } else {
                ivTwitterImage.setVisibility(View.VISIBLE);
                Glide.with(ivTwitterImage.getContext()).load(tweet.bodyImage).into(ivTwitterImage);
            }
            //Bind method is where you personalize individual clicks
            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if not already favorite

                    //tell Twitter I want to favorite this
                    //change the drawable to btn_star_big_on
                    //change the text inside tvFavoriteCount



                    //else if already favorite tell twitter I want to unfavorite this
                    //change the drawable back to btn_star_bug_off
                    //decrement the text in tvFavoriteCount
                }
            });




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
