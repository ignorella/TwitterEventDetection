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

    private static DeepLearningUtils deepLearningUtils = new DeepLearningUtils();
    private static SQLUtils sqlUtils = new SQLUtils();
    private static CommonUtils commonUtils = new CommonUtils();

    public static void main(String[] args)
    {
        log.info("::::::::::::::...EventDetection Main Class Start....::::::::::::::");

/*
        String sourceFilePath = "C:\\Users\\TY\\Desktop\\tweetFile.csv";
        String destinationFilePath = "paragraphVectorModel";

        Constants.labelList = sqlUtils.writeTweetsToFile(sourceFilePath, true);

        Word2Vec word2vec = deepLearningUtils.word2VecModel(sourceFilePath);
        deepLearningUtils.writeWord2VecModelToFile(word2vec, destinationFilePath);
        Collection<String> list = word2vec.wordsNearest("Assault", 6);
        for(String s : list)
        {
            log.debug("Word2Vec Nearest labels: " + s);
        }

        ParagraphVectors paragraphVectors = deepLearningUtils.paragraphVectorModel(sourceFilePath);
        deepLearningUtils.writeParagraphVecModelToFile(paragraphVectors, destinationFilePath);
        Collection<String> list2 = paragraphVectors.nearestLabels("Women rights", 6);
        for(String s : list2)
        {
            log.debug("ParagraphVectors Nearest labels: " + s);
        }

        commonUtils.finish();

*/
        sqlUtils.insertToTweetsTable();
        log.info("::::::::::::::...EventDetection Main Class Finish...::::::::::::::");
    }

    public void runModel()
    {
        String dataFilePath = "C:\\Users\\TY\\Desktop\\tweetFile.csv";
        //String dataFilePath = "raw_sentences.txt";
        //ClassPathResource resource = new ClassPathResource(datafilepath);
        //File file = resource.getFile();
        Constants.labelList = sqlUtils.writeTweetsToFile(dataFilePath, true);

        ParagraphVectors paragraphVector = deepLearningUtils.paragraphVectorModel(dataFilePath);

        ClusterSet clusterSetParagraphVector = deepLearningUtils.getKMeansClusterSet(paragraphVector);
        List<Cluster> clusterListParagraphVector = clusterSetParagraphVector.getMostPopulatedClusters(5);

        log.debug("##############################################################");
        log.debug("Paragraph Vectors - Cluster Centers:");
        log.debug("##############################################################");

        for(Cluster c: clusterListParagraphVector)
        {
            Point center = c.getCenter();
            Collection<String> nearestLabels = paragraphVector.nearestLabels(center.getArray(), 5);

            log.debug("--------------------------------------------------------------");
            for(String label : nearestLabels)
            {
                String[] labelArray = label.split("_");
                String tweet = sqlUtils.getSingleTweetByID(labelArray[2]);
                log.debug(label + ": " + tweet);
            }
            log.debug("--------------------------------------------------------------");
        }

        log.debug("##############################################################");
        log.debug("Paragraph Vectors End...");
        log.debug("--------------------------------------------------------------");

        paragraphVector = null;
        clusterSetParagraphVector = null;

        Word2Vec word2vec = deepLearningUtils.word2VecModel(dataFilePath);
        ClusterSet clusterSetWord2Vec = deepLearningUtils.getKMeansClusterSet(word2vec);
        List<Cluster> clusterListWord2Vec = clusterSetWord2Vec.getMostPopulatedClusters(5);

        log.debug("##############################################################");
        log.debug("Word2Vec - Cluster Centers:");
        log.debug("##############################################################");

        for(Cluster c: clusterListWord2Vec)
        {
            Point center = c.getCenter();
            Collection<String> nearestLabels = word2vec.wordsNearest(center.getArray(), 10);

            log.debug("--------------------------------------------------------------");
            for(String label : nearestLabels)
            {
                log.debug(label);
            }
            log.debug("--------------------------------------------------------------");
        }

        log.debug("##############################################################");
        log.debug("Word2Vec End...");
        log.debug("--------------------------------------------------------------");

        word2vec = null;
        clusterSetWord2Vec = null;
    }
}
