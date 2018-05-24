package com.tr.gtu.deeplearning.twitter.jdbc;

import com.tr.gtu.deeplearning.twitter.objects.MeToo;
import com.tr.gtu.deeplearning.twitter.objects.User;
import com.tr.gtu.deeplearning.twitter.objects.Tweet;
import com.tr.gtu.deeplearning.twitter.utils.CommonUtils;
import com.tr.gtu.deeplearning.twitter.utils.Constants;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
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
            String sql = "SELECT DISTINCT User FROM " + Constants.tweetTableName;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String user = rs.getString(1);
                User newTwitterUser = new User(user);
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
            String sql = "SELECT id, user, date, tweet, hashtags FROM  " + Constants.tweetTableName
                    + " WHERE User = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String id = rs.getString(1);
                String user = rs.getString(2);
                Timestamp date = rs.getTimestamp(3);
                String tweet = rs.getString(4);
                String hashtags = rs.getString(5);

                Tweet newTweet = new Tweet(id, user, date, tweet, hashtags);
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

    public String getTweetByID (String id)
    {
        String tweet = "";

        try
        {
            String sql = "SELECT tweet FROM  " + Constants.tweetTableName + " WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                tweet = rs.getString(1);
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred...", e);
        }

        return tweet;
    }

    public List<String> writeTweetsToFile(String filePath, boolean ignoreRetweets)
    {
        log.debug("Tweets will be written to the file: " + filePath);
        List<String> labelList = new ArrayList<String>();

        try
        {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            String sql = "SELECT tweet, id, user FROM " + Constants.tweetTableName
                    + (ignoreRetweets ? " WHERE tweet not like 'RT%'" : "")
                    + " ORDER BY ID LIMIT " + Constants.tweetCountLimit;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String tweet = rs.getString(1);
                String id = rs.getString(2);
                String user = rs.getString(3);

                labelList.add("TWEET_" + user + "_" + id);
                writer.println(tweet);
            }

            writer.close();
            writer.flush();
            rs.close();
            stmt.close();
            log.debug("Writing Tweets to file is finished.");
        }
        catch (Exception e)
        {
            log.error("An error occurred...", e);
        }

        return labelList;
    }

    public List<String> insertTweetVectors(String id, String method, String vectorStr)
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
            stmt.setString(3, id);
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

    public int insertMeTooObject(MeToo object)
    {
        int affectedRows = 0;

        try
        {
            String sql = "INSERT INTO " + Constants.meTooTable
                    + " SELECT ?, ?, ?, ?, ?, ?, ?, ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, object.getId());
            stmt.setString(2, object.getInsertdate());
            stmt.setString(3, object.getUsername());
            stmt.setString(4, object.getFollowers());
            stmt.setString(5, object.getHashtagsearched());
            stmt.setString(6, object.getTweetid());
            stmt.setString(7, object.getDateoftweet());
            stmt.setString(8, object.getText());

            affectedRows = stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e)
        {
            log.error("An error occurred while inserting Tweet data...", e);
        }

        return affectedRows;
    }

    /*
     *   Usage: SELECT ID, User, Date, Text FROM TABLE
     *   Preprocess Text --> links, @accounts, punctuation(except #) removed from text
     *   Extract #hashtags --> #hashtag1;#hashtag2;.....
     */
    public void insertToTweetsTable()
    {
        try
        {
            String sql = "select id, UserID, dateoftweet, Text from metoo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while(rs.next())
            {
                String id = rs.getString(1);
                String user = rs.getString(2);
                String dateStr = rs.getString(3);
                String tweet = rs.getString(4);

                List<String> hashtags = commonUtils.retrieveHashtags(tweet);
                tweet = commonUtils.preprocessTweet(tweet);
                String hashtagStr = (hashtags != null && !hashtags.isEmpty()) ? String.join(";", hashtags) : "";
                Timestamp date = new Timestamp(Constants.csvDateFormatTweet.parse(dateStr).getTime());

                String insertSql = "INSERT INTO tweets SELECT ?, ?, ?, ?, ?";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1,id);
                insertStmt.setString(2,user);
                insertStmt.setTimestamp(3,date);
                insertStmt.setString(4,tweet);
                insertStmt.setString(5,hashtagStr);

                count += insertStmt.executeUpdate();
            }

            rs.close();
            stmt.close();

            log.debug("==================== INSERTED: " + count);
        }
        catch (Exception e)
        {
            log.error("An error occurred while writing Tweets to file...", e);
        }
    }








}
