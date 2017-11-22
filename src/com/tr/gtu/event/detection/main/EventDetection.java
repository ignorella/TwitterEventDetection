package com.tr.gtu.event.detection.main;

import com.tr.gtu.event.detection.utils.CommonUtils;
import com.tr.gtu.event.detection.utils.DeepLearningUtils;
import com.tr.gtu.event.detection.utils.SQLUtils;
import org.apache.log4j.Logger;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.util.Collection;

/**
 * Created by TY.
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

        String sourceFilePath = "C:\\Users\\TY\\Desktop\\tweetFile.csv";
        String destinationFilePath = "paragraphVectorModel";

        Constants.labelList = sqlUtils.writeTweetsToFile(sourceFilePath, true);

        Word2Vec word2vec = deepLearningUtils.word2VecModel(sourceFilePath);
        deepLearningUtils.writeWord2VecModelToFile(word2vec, destinationFilePath);
        Collection<String> list = word2vec.wordsNearest("Galatasaray", 6);
        for(String s : list)
        {
            log.debug("Word2Vec Nearest labels: " + s);
        }

        ParagraphVectors paragraphVectors = deepLearningUtils.paragraphVectorModel(sourceFilePath);
        deepLearningUtils.writeParagraphVecModelToFile(paragraphVectors, destinationFilePath);
        Collection<String> list2 = paragraphVectors.nearestLabels("Galatasaray Fenerbahçe maçı pazar günü", 6);
        for(String s : list2)
        {
            log.debug("ParagraphVectors Nearest labels: " + s);
        }



        commonUtils.finish();

        log.info("::::::::::::::...EventDetection Main Class Finish...::::::::::::::");
    }
}
