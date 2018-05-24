package com.tr.gtu.deeplearning.twitter.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class User
{
    private String userID;
    private List<Tweet> tweetList;

    public User(String userID)
    {
        this.userID = userID;
        this.tweetList = new ArrayList<Tweet>();
    }

    public void addTweets(Tweet newTweet){
        this.tweetList.add(newTweet);
    }

    /*
     * GETTER AND SETTER METHODS
     */
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

}
