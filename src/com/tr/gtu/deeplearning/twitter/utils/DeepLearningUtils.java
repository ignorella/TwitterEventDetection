package com.tr.gtu.deeplearning.twitter.utils;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.clustering.kmeans.KMeansClustering;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class DeepLearningUtils
{
    private static Logger log = Logger.getLogger(DeepLearningUtils.class);

    public ParagraphVectors paragraphVectorModel(String filePath)
    {
        ParagraphVectors vec = null;

        try
        {
            log.info("Starting ParagraphVectors...");

            File file = new File(filePath);
            SentenceIterator iterator = new BasicLineIterator(file);
            AbstractCache<VocabWord> cache = new AbstractCache<>();
            TokenizerFactory t = new DefaultTokenizerFactory();
            t.setTokenPreProcessor(new CommonPreprocessor());

            log.info("Iterator and tokenizer are ready... starting vector model");
        /*
             if you don't have LabelAwareIterator handy, you can use synchronized labels generator
              it will be used to label each document/sequence/line with it's own label.

              But if you have LabelAwareIterator ready, you can can provide it, for your in-house labels
        */
            LabelsSource source = new LabelsSource("TWEET_");

            vec = new ParagraphVectors.Builder()
                    .minWordFrequency(DeepLearningCostants.MIN_WORD_FREQUENCY)
                    .iterations(DeepLearningCostants.ITERATION_COUNT)
                    .epochs(DeepLearningCostants.EPOCHS)
                    .layerSize(DeepLearningCostants.LAYER_SIZE)
                    .learningRate(DeepLearningCostants.LEARNING_RATE)
                    //.labelsSource(source)
                    .labels(Constants.labelList)
                    .windowSize(DeepLearningCostants.WINDOW_SIZE)
                    .iterate(iterator)
                    .trainWordVectors(false)
                    .vocabCache(cache)
                    .tokenizerFactory(t)
                    .sampling(DeepLearningCostants.SAMPLING)
                    .build();

            vec.fit();
            log.info("Finished constructing ParagraphVectors model. Returning model...");
        }
        catch (Exception e)
        {
            log.error("An error occurred during Paragraph Vectors process...", e);
        }

        return vec;
    }

    public Word2Vec word2VecModel(String filePath)
    {
        Word2Vec vec = null;

        try
        {
            log.info("Starting Word2Vec model...");

            File file = new File(filePath);
            SentenceIterator iterator = new BasicLineIterator(file);
            AbstractCache<VocabWord> cache = new AbstractCache<>();
            TokenizerFactory t = new DefaultTokenizerFactory();
            t.setTokenPreProcessor(new CommonPreprocessor());

            log.info("Iterator and tokenizer are ready... starting vector model");
        /*
             if you don't have LabelAwareIterator handy, you can use synchronized labels generator
              it will be used to label each document/sequence/line with it's own label.

              But if you have LabelAwareIterator ready, you can can provide it, for your in-house labels
        */
            vec = new Word2Vec.Builder()
                    .minWordFrequency(DeepLearningCostants.MIN_WORD_FREQUENCY)
                    .iterations(DeepLearningCostants.ITERATION_COUNT)
                    .epochs(DeepLearningCostants.EPOCHS)
                    .layerSize(DeepLearningCostants.LAYER_SIZE)
                    .learningRate(DeepLearningCostants.LEARNING_RATE)
                    .windowSize(DeepLearningCostants.WINDOW_SIZE)
                    .iterate(iterator)
                    .vocabCache(cache)
                    .tokenizerFactory(t)
                    .sampling(DeepLearningCostants.SAMPLING)
                    .build();

            vec.fit();
            log.info("Finished constructing Word2Vec model. Returning model...");
        }
        catch (Exception e)
        {
            log.error("An error occurred during Word2Vec process...", e);
        }

        return vec;
    }

    public ClusterSet getKMeansClusterSet(Word2Vec vector)
    {
        //1. create a kmeanscluster instance
        KMeansClustering kmc = KMeansClustering.setup(DeepLearningCostants.KMEANS_CLUSTER_COUNT, DeepLearningCostants.KMEANS_ITERATION_COUNT,
                DeepLearningCostants.KMEANS_DISTANCE_FUNCTION);
        StopWatch sw = new StopWatch();

        //2. iterate over rows in the vector and create a List of vectors
        List<Point> pointsLst = new ArrayList<Point>();

        for (String word : vector.vocab().words()) {
            Point point = new Point(vector.getWordVectorMatrix(word));
            point.setLabel(word);
            pointsLst.add(point);
        }

        log.info("Start Clustering for " + pointsLst.size() + " points...");

        sw.reset();
        sw.start();
        ClusterSet cs = kmc.applyTo(pointsLst);
        sw.stop();

        log.info("Time taken to run clustering on vectors: " + sw.getTime());
        pointsLst = null;
        return cs;
    }

    public void writeWord2VecModelToFile(Word2Vec vec, String filePath)
    {
        try
        {
            WordVectorSerializer.writeWordVectors(vec, filePath);
        }
        catch (IOException e)
        {
            log.error("An error occurred while writing Word2Vec model to file...", e);
        }
    }

    public void writeParagraphVecModelToFile(ParagraphVectors vec, String filePath)
    {
        //WordVectorSerializer.writeParagraphVectors(vec, filePath + ".zip");
        WordVectorSerializer.writeWordVectors(vec, filePath + ".txt");
    }
}
