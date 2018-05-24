package com.tr.gtu.deeplearning.twitter.objects;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class Tweet
{
    private String id;
    private String user;
    private Timestamp date;
    private String tweet;
    private String hashtags;
    private List<Double> tweetVector = new ArrayList<Double>();

    public Tweet(){}

    public Tweet (String id, String user, Timestamp date, String tweet, String hashtags)
    {
        this.id = id;
        this.user = user;
        this.date = date;
        this.tweet = tweet;
        this.hashtags = hashtags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public List<Double> getTweetVector() {
        return tweetVector;
    }

    public void setTweetVector(List<Double> tweetVector) {
        this.tweetVector = tweetVector;
    }
}
