package com.tr.gtu.deeplearning.twitter.utils;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class TwitterUtils {

/*

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKeyStr);
        cb.setOAuthConsumerSecret(consumerSecretStr);
        cb.setOAuthAccessToken(accessTokenStr);
        cb.setOAuthAccessTokenSecret(accessTokenSecretStr);






        twitter4j.TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        FilterQuery fq = new FilterQuery();
        String keywords[] = {"Turkey", "Istanbul"};

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);

*/


}
