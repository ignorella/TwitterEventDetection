package com.tr.gtu.event.detection.main;

import com.tr.gtu.event.detection.utils.CommonUtils;
import com.tr.gtu.event.detection.utils.DeepLearningUtils;
import com.tr.gtu.event.detection.utils.SQLUtils;
import org.apache.log4j.Logger;
import org.deeplearning4j.clustering.cluster.Cluster;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.util.Collection;
import java.util.List;

//import org.synthesis.java.extension.ParagraphVectorSerializer;

public class FileUtils
{
    private static Logger log = Logger.getLogger(FileUtils.class);

    private static DeepLearningUtils deepLearningUtils = new DeepLearningUtils();
    private static SQLUtils sqlUtils = new SQLUtils();
    private static CommonUtils commonUtils = new CommonUtils();

    public static void main(String[] args) throws Exception
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

            for(String label : nearestLabels)
            {
                String[] labelArray = label.split("_");
                String tweet = sqlUtils.getSingleTweetByID(labelArray[2]);
                log.debug(label + ": " + tweet);
            }
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

            for(String label : nearestLabels)
            {
                log.debug(label);
            }
        }

        log.debug("##############################################################");
        log.debug("Word2Vec End...");
        log.debug("--------------------------------------------------------------");

        word2vec = null;
        clusterSetWord2Vec = null;
    }
}
