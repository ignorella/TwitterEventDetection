package com.tr.gtu.deeplearning.twitter.main;

import com.tr.gtu.deeplearning.twitter.jdbc.SQLUtils;
import com.tr.gtu.deeplearning.twitter.utils.CommonUtils;
import com.tr.gtu.deeplearning.twitter.utils.DeepLearningUtils;
import com.tr.gtu.deeplearning.twitter.utils.Constants;
import org.apache.log4j.Logger;
import org.deeplearning4j.clustering.cluster.Cluster;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.util.Collection;
import java.util.List;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class EventDetection
{
    private static Logger log = Logger.getLogger(EventDetection.class);
    private static SQLUtils sqlUtils = new SQLUtils();
    private static CommonUtils commonUtils = new CommonUtils();
    private static DeepLearningUtils deepLearningUtils = new DeepLearningUtils();
    private static Scenarios scenarios = new Scenarios();

    public static void main(String[] args)
    {
        log.info("::::::::::::::...EventDetection Main Class Start....::::::::::::::");

        Constants.inputFile = "C:\\Users\\TY\\Desktop\\tweetFile.csv";
        Constants.modelFileParagraphVec = "paragraphVectorModel";
        Constants.modelFileWord2Vec = "word2VecModel";

        /*
        Constants.labelList = sqlUtils.writeTweetsToFile(Constants.inputFile, true);
        scenarios.runModelParagraphVectorsWithKMeans(Constants.inputFile, 5, 5, true);
        scenarios.runModelWord2VecWithKMeans(Constants.inputFile, 5, 5, true);
        */

        try {
            deepLearningUtils.sequenceVector("");
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        commonUtils.finish();
        log.info("::::::::::::::...EventDetection Main Class Finish...::::::::::::::");
    }


}
