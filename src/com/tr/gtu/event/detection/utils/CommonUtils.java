package com.tr.gtu.event.detection.utils;

import com.tr.gtu.event.detection.main.Constants;
import com.tr.gtu.event.detection.objects.Tweet;
import com.tr.gtu.event.detection.objects.TwitterUser;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TY.
 */
public class CommonUtils
{
    private static Logger log = Logger.getLogger(CommonUtils.class);

    public static SQLUtils sqlUtils = new SQLUtils();

    public void fillUserList()
    {
        sqlUtils.getTwitterUsers();
    }

    public void fillUserTweetList()
    {
        for(TwitterUser user : Constants.allUsers)
        {
            List<Tweet> tweetList = new ArrayList<Tweet>();
            tweetList = sqlUtils.getTweetsByUser(user.getUserID());
            user.setTweetList(tweetList);
        }
    }


    public void fillUserVectorTable(String filePath, String method)
    {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;

        try
        {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = "";

            while((line = bufferedReader.readLine()) != null)
            {
                String[] lineArray = line.split(" ");
                String[] labelArray = lineArray[1].split("_");
                String vector = "";

                for(int i = 2; i < lineArray.length; i++)
                {
                    vector = lineArray[i] + ((i == lineArray.length - 1) ? "" : " ");
                }
            }
        }
        catch (Exception e)
        {
            log.error("An error occurred during File operations...", e);
        }
    }

    public String preprocessTweet(String tweet)
    {
        String preprocessedTweet = "";
        String [] tweetArray = tweet.split(" ");

        for(int i = 0; i < tweetArray.length; i++)
        {
            if(tweetArray[i].contains("@")
                    || tweetArray[i].contains("http")
                    || tweetArray[i].equalsIgnoreCase("RT")
                    || tweetArray[i].contains(";")
                    || tweetArray[i].contains("!")
                    || tweetArray[i].contains(".")
                    || tweetArray[i].contains(",")
                    || tweetArray[i].contains("?")
                    || tweetArray[i].contains("/")
                    || tweetArray[i].contains("(")
                    || tweetArray[i].contains(")"))
                continue;

            preprocessedTweet += tweetArray[i] + ((i == tweetArray.length - 1) ? "" : " ");
        }

        return preprocessedTweet;
    }

    public static void finish()
    {
        sqlUtils.disconnect();
    }
}
