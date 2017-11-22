package com.tr.gtu.event.detection.utils;

import com.tr.gtu.event.detection.main.Constants;
import com.tr.gtu.event.detection.objects.Tweet;
import com.tr.gtu.event.detection.objects.TwitterUser;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TY.
 */
public class SQLUtils
{
    private static Logger log = Logger.getLogger(SQLUtils.class);

    private Connection conn = JDBCConnection.instance();
    private CommonUtils commonUtils = new CommonUtils();

    public void disconnect()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            log.error("An error occurred while disconnecting from database connection...", e);
        }
    }

    public void getTwitterUsers ()
    {
        try
        {
            String sql = "SELECT DISTINCT UserID FROM " + Constants.tweetTableName;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String userID = rs.getString(1);
                TwitterUser newTwitterUser = new TwitterUser(userID);
                Constants.allUsers.add(newTwitterUser);
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred while getting Twitter users...", e);
        }
    }

    public List<Tweet> getTweetsByUser (String userID)
    {
        List<Tweet> tweetList = new ArrayList<Tweet>();

        try
        {
            String sql = "SELECT * FROM  " + Constants.tweetTableName + " WHERE UserID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String screenName = rs.getString("SentByScreenName");
                String text = rs.getString("Text");
                String source = rs.getString("Source");
                String createdAt = rs.getString("DateCreatedAt");
                String country = rs.getString("PlaceCountry");
                int retweetCount = Integer.valueOf(rs.getString("RetweetCount"));
                boolean isRetweet = (rs.getString("IsRetweet").equals("1")) ? true : false;
                String id = rs.getString("ID");

                Tweet newTweet = new Tweet(screenName, text, source, createdAt, country, retweetCount, isRetweet, id);
                tweetList.add(newTweet);
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred while getting Tweets by user...", e);
        }

        return tweetList;
    }

    public String getSingleTweetByID (String tweetID)
    {
        String tweet = "";

        try
        {
            String sql = "SELECT Text FROM  " + Constants.tweetTableName + " WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tweetID);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                tweet = rs.getString("Text");
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred while getting a single Tweet by ID...", e);
        }

        return tweet;
    }

    public List<String> writeTweetsToFile(String filePath, boolean doPreprocessing)
    {
        System.out.println("Tweets will be written to the file: " + filePath);
        List<String> labelList = new ArrayList<String>();

        try
        {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            String sql = "SELECT Text, ID, UserID FROM " + Constants.tweetTableName + " ORDER BY ID LIMIT " + Constants.tweetCountLimit;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String tweetStr = rs.getString(1);
                String tweetId = rs.getString(2);
                String userId = rs.getString(3);

                labelList.add("TWEET_" + userId + "_" + tweetId);
                tweetStr = tweetStr.replace("\n", " ").replace("\r", " ");

                if(doPreprocessing)
                {
                    tweetStr = commonUtils.preprocessTweet(tweetStr);
                }

                writer.println(tweetStr);
            }

            writer.close();
            writer.flush();
            rs.close();
            stmt.close();
            System.out.println("Writing Tweets to file is finished.");
        }
        catch (Exception e)
        {
            log.error("An error occurred while writing Tweets to file...", e);
        }

        return labelList;
    }

    public List<String> insertTweetVectors(String userID, String tweetID, String method, String vectorStr)
    {
        List<String> labelList = new ArrayList<String>();

        try
        {
            String sql = "INSERT INTO " + Constants.vectorTableName
                    + " SELECT UserID, ID, ?, ?"
                    + " FROM " + Constants.tweetTableName + " WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, method);
            stmt.setString(2, vectorStr);
            stmt.setString(3, tweetID);
            ResultSet rs = stmt.executeQuery();

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred while inserting Tweet vectors...", e);
        }

        return labelList;
    }
}
