package com.tr.gtu.event.detection.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TY.
 */
public class Tweet
{
    private String sentByScreenName;
    //private String sentBy;
    //private int inReplyToStatusId;
    //private String inReplyToUserId;
    //private String inReplyToScreenName;
    private String text;
    private String source;
    private String dateCreatedAt;
    //private String mediaEntities;
    private String placeCountry;
    //private String placeName;
    //private String placeStreet;
    private int retweetCount;
    //private String retweetedStatusSource;
    //private Date retweetedStatusText;
    //private String userMentionEntities;
    private boolean isRetweet;
    //private boolean isRetweetedByMe;
    private String id;
    //private int userID;
    private List<Double> tweetVector = new ArrayList<Double>();

    public Tweet(){}

    public Tweet (String screenName, String text, String source, String createdAt, String placeCountry, int retweetCount, boolean isRetweet, String id)
    {
        this.sentByScreenName = screenName;
        this.text = text;
        this.source = source;
        this.dateCreatedAt = createdAt;
        this.placeCountry = placeCountry;
        this.retweetCount = retweetCount;
        this.isRetweet = isRetweet;
        this.id = id;
    }

    public String getSentByScreenName() {
        return sentByScreenName;
    }

    public void setSentByScreenName(String sentByScreenName) {
        this.sentByScreenName = sentByScreenName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateCreatedAt() {
        return dateCreatedAt;
    }

    public void setDateCreatedAt(String dateCreatedAt) {
        this.dateCreatedAt = dateCreatedAt;
    }

    public String getPlaceCountry() {
        return placeCountry;
    }

    public void setPlaceCountry(String placeCountry) {
        this.placeCountry = placeCountry;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getTweetVector() {
        return tweetVector;
    }

    public void setTweetVector(List<Double> tweetVector) {
        this.tweetVector = tweetVector;
    }
}
