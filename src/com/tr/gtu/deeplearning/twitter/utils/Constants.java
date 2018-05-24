package com.tr.gtu.deeplearning.twitter.utils;

import com.tr.gtu.deeplearning.twitter.objects.Tweet;
import com.tr.gtu.deeplearning.twitter.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class Constants
{
    public static String tweetTableName = "tweets";
    public static String vectorTableName = "usertweetvectors";

    public static List<User> allUsers = new ArrayList<User>();
    public static List<String> labelList = new ArrayList<String>();
    public static int tweetCountLimit = 8000;

    /*
     * MeToo Table
     */
    public static String meTooTable = "MeToo";
    public static String hashtagTable = "MeTooHashtags";

    public static char csvSeparator = ',';
    public static char csvQuoteCharacter = '\"';
    public static SimpleDateFormat csvDateFormatInsert = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat csvDateFormatTweet = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault());
}
