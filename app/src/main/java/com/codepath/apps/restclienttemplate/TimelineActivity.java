package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PBEKeySpec;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    private final int Request_Code = 20;
    public final String TAG = "Timeline Activity";
    private EndlessRecyclerViewScrollListener scrollListener;


    TwitterClient client;
    RecyclerView rvTweets;
    TweetsAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Init list of tweets and adapter
        adapter = new TweetsAdapter();
        //Recycler view setUp: layout manager and the adapter
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(llm);
        rvTweets.setAdapter(adapter);

        swipeContainer = findViewById(R.id.srlTimline);

        swipeContainer.setOnRefreshListener(() -> {
            adapter.clear();//clear the recycler view
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            populateHomeTimeline(null);
        });

        populateHomeTimeline(null);//Passing null becasuse we want the first 25.

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Tweet lastTweetBeingDisplayed = adapter.getTweets().get(adapter.getTweets().size()-1);
                String maxId = lastTweetBeingDisplayed.id;
                populateHomeTimeline(maxId);
            }
        };

        rvTweets.addOnScrollListener(scrollListener);
    }

    public List<Tweet> getTweets() {
        return adapter.getTweets();
    }

    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
        //Inflate menu. This add items to the action bar if present
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Compose is the twitter logo
        if (item.getItemId() == R.id.compose) {
            //Compose icon has been selected

            //Intent takes where were coming from or this and where we are going to
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, Request_Code);

            return true;
        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Request_Code && resultCode == RESULT_OK) {
            //Get data from the intent (tweet)
            Tweet tweet =Parcels.unwrap(data.getParcelableExtra("tweet"));


            //Update the RV with the tweet

            //Modify data source
            adapter.addToTop(tweet);
            //Update the adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);




        }

        super.onActivityResult(requestCode, resultCode, data);




    }

    private void populateHomeTimeline(String maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSucess " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    Log.i(TAG, "onSucess"+ json.toString());
                   // List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    swipeContainer.setRefreshing(false);//hides the spinner
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure " + response , throwable);
            }
        });
    }

    public void onLogoutButton(View view) {
        // forget who's logged in


        TwitterApp.getRestClient(this).clearAccessToken();
        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);
        finish();
    }
}