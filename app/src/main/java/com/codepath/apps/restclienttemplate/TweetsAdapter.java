package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

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
        ImageButton ibReply;



        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTwitterImage = itemView.findViewById(R.id.ivTwitterImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);
            ibReply= itemView.findViewById((R.id.ibReply));


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

            //set the ibFavoriteCount number
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));



            //set the ibFavorite to yellow or grey
            if (tweet.isFavorited) {
                Drawable newImage = itemView.getContext().getDrawable(android.R.drawable.btn_star_big_on);
                ibFavorite.setImageDrawable(newImage);
            } else{
                Drawable newImage = itemView.getContext().getDrawable(android.R.drawable.btn_star_big_off);
                ibFavorite.setImageDrawable(newImage);
            }
            //Bind method is where you personalize individual clicks



            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if not already favorite
                    if(!tweet.isFavorited) {
                        //Tell twitter I want to favorite this
                        TwitterApp.getRestClient(itemView.getContext()).favorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "This should've been favorited go check");

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                        tweet.isFavorited = true;
                        Drawable newImage = itemView.getContext().getDrawable(android.R.drawable.btn_star_big_on);
                        ibFavorite.setImageDrawable(newImage);

                        //med: increment the text inside tvFavoriteCount
                        tweet.favoriteCount = tweet.favoriteCount+1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

                    }
                    //if already favorited
                    else  {
                        //tell twitter I want to unfavorite this
                        TwitterApp.getRestClient(itemView.getContext()).unfavorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "This message shouldve been unfavorited, go check");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });

                        //change the drawable back to btn_star_big_off
                        tweet.isFavorited=false;
                        Drawable newImage = itemView.getContext().getDrawable(android.R.drawable.btn_star_big_off);
                        ibFavorite.setImageDrawable(newImage);

                        //decrement the text inside tvFavoriteCount
                        tweet.favoriteCount= tweet.favoriteCount-1;
                        tvFavoriteCount.setText((String.valueOf(tweet.favoriteCount)));
                    }
                    //Your holder should contain a member variable
                    //for any view that will be set as you render a row
                }
            });

            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Pop up a compose screen
                    //It is not gonna be a brand new tweet but it will have an extra attribute
                        //extra attribute "

                    Intent i = new Intent(itemView.getContext(), ComposeActivity.class);
                    i.putExtra("should_reply_to_tweet", true);
                    i.putExtra("id_of_tweet_to_reply_to",tweet.id);
                    i.putExtra("screenname_of_tweet_to_reply_to",tweet.user.screenName);

                    i.putExtra("tweet_to_reply_to", Parcels.wrap(tweet));
                    ((Activity) itemView.getContext()).startActivityForResult(i, TimelineActivity.Request_Code);
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
