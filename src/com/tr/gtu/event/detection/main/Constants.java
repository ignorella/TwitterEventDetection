package com.tr.gtu.event.detection.main;

import com.tr.gtu.event.detection.objects.Tweet;
import com.tr.gtu.event.detection.objects.TwitterUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TY.
 */
public class Constants
{
    public static String consumerKeyStr = "RxWUBvKLr8C1OedhhwIzE1Sch";
    public static String consumerSecretStr = "bHvVaL9E7rB2ufHAzy9wwCwpP2s3XDM7GBA8sRCWs9vKMRbIGk";
    public static String accessTokenStr = "84376935-uQNIetXzH5khLaiMpHzNPE8GizHQBOdl07EHxSwe7";
    public static String accessTokenSecretStr = "1mJNjEKgDYg6206IG9CA4UNdUZu0vxoPgHLWPrCyQlDM2";

    public static String tweetTableName = "usertweet10000";
    //public static String tweetTableName = "usertweet3000";
    public static String vectorTableName = "usertweetvectors";

    public static List<Tweet> allTweets = new ArrayList<Tweet>();
    public static List<TwitterUser> allUsers = new ArrayList<TwitterUser>();
    public static List<String> labelList = new ArrayList<String>();
    public static int tweetCountLimit = 5000000;

    /*
     * Deep Learning Model Constants
     */
    public static double LEARNING_RATE = 0.025;
    public static int ITERATION_COUNT = 100;
    public static int LAYER_SIZE = 100;
    public static int EPOCHS = 5;
    public static int WINDOW_SIZE = 5;
    public static int MIN_WORD_FREQUENCY = 10;
    public static int SAMPLING = 0;

    public static int KMEANS_ITERATION_COUNT = 10;
    public static int KMEANS_CLUSTER_COUNT = 30;
    public static String KMEANS_DISTANCE_FUNCTION = "cosinesimilarity";
}
