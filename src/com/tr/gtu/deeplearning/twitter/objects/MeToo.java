package com.tr.gtu.deeplearning.twitter.objects;

import java.util.Date;

/**
 * Created by TY on 21.05.2018.
 */
public class MeToo
{
    private String id;
    private String insertdate ;
    private String username ;
    private String followers;
    private String hashtagsearched;
    private String tweetid;
    private String dateoftweet;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getHashtagsearched() {
        return hashtagsearched;
    }

    public void setHashtagsearched(String hashtagsearched) {
        this.hashtagsearched = hashtagsearched;
    }

    public String getTweetid() {
        return tweetid;
    }

    public void setTweetid(String tweetid) {
        this.tweetid = tweetid;
    }

    public String getDateoftweet() {
        return dateoftweet;
    }

    public void setDateoftweet(String dateoftweet) {
        this.dateoftweet = dateoftweet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
