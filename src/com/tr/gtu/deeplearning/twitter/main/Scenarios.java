package com.tr.gtu.deeplearning.twitter.main;

import com.tr.gtu.deeplearning.twitter.jdbc.SQLUtils;
import com.tr.gtu.deeplearning.twitter.utils.CommonUtils;
import com.tr.gtu.deeplearning.twitter.utils.Constants;
import com.tr.gtu.deeplearning.twitter.utils.DeepLearningUtils;
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
public class Scenarios
{
    private static Logger log = Logger.getLogger(Scenarios.class);
    private static DeepLearningUtils deepLearningUtils = new DeepLearningUtils();
    private static SQLUtils sqlUtils = new SQLUtils();

    public void runModelParagraphVectorsWithKMeans(String inputFile, int mostPopulatedCount, int nearestPointsCount, boolean saveModel)
    {
        log.debug("##############################################################");
        log.debug("Paragraph Vectors - CREATING MODEL...");
        log.debug("##############################################################");

        ParagraphVectors paragraphVector = deepLearningUtils.paragraphVectorModel(inputFile);
        ClusterSet clusterSetParagraphVector = deepLearningUtils.getKMeansClusterSet(paragraphVector);
        List<Cluster> clusterListParagraphVector = clusterSetParagraphVector.getMostPopulatedClusters(mostPopulatedCount);

        log.debug("--------------------------------------------------------------");
        log.debug("Paragraph Vectors - Cluster Centers:");
        log.debug("##############################################################");

        for(Cluster c: clusterListParagraphVector)
        {
            Point center = c.getCenter();
            Collection<String> nearestLabels = paragraphVector.nearestLabels(center.getArray(), nearestPointsCount);

            log.debug("--------------------------------------------------------------");
            for(String label : nearestLabels)
            {
                String[] labelArray = label.split("_");
                String tweet = sqlUtils.getTweetByID(labelArray[2]);
                log.debug(label + ": " + tweet);
            }
            log.debug("--------------------------------------------------------------");
        }

        if(saveModel)
            deepLearningUtils.writeParagraphVecModelToFile(paragraphVector, Constants.modelFileParagraphVec + Constants.modelExtentionParagraphVec);

        log.debug("##############################################################");
        log.debug("Paragraph Vectors End...");
        log.debug("##############################################################");

        paragraphVector = null;
        clusterSetParagraphVector = null;
    }

    public void runModelWord2VecWithKMeans(String inputFile, int mostPopulatedCount, int nearestPointsCount, boolean saveModel)
    {
        log.debug("##############################################################");
        log.debug("Word2Vec - CREATING MODEL...");
        log.debug("##############################################################");

        Word2Vec word2vec = deepLearningUtils.word2VecModel(inputFile);
        ClusterSet clusterSetWord2Vec = deepLearningUtils.getKMeansClusterSet(word2vec);
        List<Cluster> clusterListWord2Vec = clusterSetWord2Vec.getMostPopulatedClusters(mostPopulatedCount);

        log.debug("--------------------------------------------------------------");
        log.debug("Word2Vec - Cluster Centers:");
        log.debug("##############################################################");

        for(Cluster c: clusterListWord2Vec)
        {
            Point center = c.getCenter();
            Collection<String> nearestLabels = word2vec.wordsNearest(center.getArray(), nearestPointsCount);

            log.debug("--------------------------------------------------------------");
            for(String label : nearestLabels)
            {
                log.debug(label);
            }
            log.debug("--------------------------------------------------------------");
        }

        if(saveModel)
            deepLearningUtils.writeWord2VecModelToFile(word2vec, Constants.modelFileWord2Vec + Constants.modelExtentionWord2Vec);

        log.debug("##############################################################");
        log.debug("Word2Vec End...");
        log.debug("##############################################################");

        word2vec = null;
        clusterSetWord2Vec = null;
    }
}
