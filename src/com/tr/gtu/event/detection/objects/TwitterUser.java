package com.tr.gtu.event.detection.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TY.
 */
public class TwitterUser
{
    private String userID;
    private List<Tweet> tweetList;

    public TwitterUser(String userID)
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
