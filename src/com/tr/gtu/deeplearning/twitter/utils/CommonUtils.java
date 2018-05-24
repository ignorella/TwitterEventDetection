package com.tr.gtu.deeplearning.twitter.utils;

import au.com.bytecode.opencsv.CSVReader;
import com.tr.gtu.deeplearning.twitter.jdbc.SQLUtils;
import com.tr.gtu.deeplearning.twitter.objects.MeToo;
import com.tr.gtu.deeplearning.twitter.objects.User;
import com.tr.gtu.deeplearning.twitter.objects.Tweet;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class CommonUtils
{
    private static Logger log = Logger.getLogger(CommonUtils.class);

    private static SQLUtils sqlUtils = new SQLUtils();

    public void fillUserList()
    {
        sqlUtils.getTwitterUsers();
    }

    public void fillUserTweetList()
    {
        for(User user : Constants.allUsers)
        {
            List<Tweet> tweetList = new ArrayList<Tweet>();
            tweetList = sqlUtils.getTweetsByUser(user.getUserID());
            user.setTweetList(tweetList);
        }
    }

    public void readMeTooFile(String filename)
    {
        int counter = 0;
        int skipped = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            for(String line; (line = br.readLine()) != null; )
            {
                if(counter == 0)
                {
                    counter++;
                    continue;
                }

                while(!line.endsWith("\""))
                {
                    String nextLine = br.readLine();
                    line += " " + nextLine;
                }

                CSVReader reader = new CSVReader(new StringReader(line), Constants.csvSeparator, Constants.csvQuoteCharacter);
                List<String[]>  csvLines = reader.readAll();

                if(csvLines == null || csvLines.isEmpty())
                {
                    //log.warn("LINE ERROR... Line: " + String.join("____", line));
                    skipped++;
                    continue;
                }

                String [] csvLine = csvLines.get(0);

                if(csvLine.length != 14)
                {
                    //log.warn("Line skipped... Size: " + csvLine.length + " Line: " + String.join("____", csvLine));
                    skipped++;
                    continue;
                }

                MeToo object = new MeToo();

                object.setId(csvLine[0] != null ? csvLine[0] : "");
                object.setInsertdate(csvLine[1]);
                object.setUsername(csvLine[2] != null ? csvLine[2] : "");
                object.setFollowers(csvLine[3] != null ? csvLine[3] : "");
                object.setHashtagsearched(csvLine[4] != null ? csvLine[4] : "");
                object.setTweetid(csvLine[5] != null ? csvLine[5] : "");
                object.setDateoftweet(csvLine[6]);
                object.setText(csvLine[7] != null ? csvLine[7] : "");

                counter += sqlUtils.insertMeTooObject(object);
            }
        }
        catch (Exception e)
        {
            log.error("An error occurred...", e);
        }

        log.info("Number of objects inserted: " + (counter - 1));
        log.info("Number of skipped objects: " + skipped);
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
        tweet = tweet.replace("\n", " ").replace("\r", " ");
        String [] tweetArray = tweet.split(" ");

        for(int i = 0; i < tweetArray.length; i++)
        {
            if(!tweetArray[i].startsWith("@")  && !tweetArray[i].startsWith("http") )
                preprocessedTweet += tweetArray[i].replaceAll("[\\p{Punct}&&[^#]]", "") + ((i == tweetArray.length - 1) ? "" : " ");
        }

        return preprocessedTweet;
    }

    public List<String> retrieveHashtags(String tweet)
    {
        List<String> hashtags = new ArrayList<String>();
        String [] tweetArray = tweet.split(" ");

        for(int i = 0; i < tweetArray.length; i++)
        {
            if(tweetArray[i].startsWith("#"))
                hashtags.add(tweetArray[i]);
        }

        return hashtags;
    }

    public static void finish()
    {
        sqlUtils.disconnect();
    }
}
