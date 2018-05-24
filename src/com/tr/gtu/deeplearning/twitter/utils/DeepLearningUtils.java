package com.tr.gtu.deeplearning.twitter.utils;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.clustering.kmeans.KMeansClustering;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.learning.impl.elements.SkipGram;
import org.deeplearning4j.models.embeddings.loader.VectorsConfiguration;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.sequencevectors.SequenceVectors;
import org.deeplearning4j.models.sequencevectors.iterators.AbstractSequenceIterator;
import org.deeplearning4j.models.sequencevectors.transformers.impl.SentenceTransformer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabConstructor;
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
                    .minWordFrequency(DeepLearningConstants.MIN_WORD_FREQUENCY)
                    .iterations(DeepLearningConstants.ITERATION_COUNT)
                    .epochs(DeepLearningConstants.EPOCHS)
                    .layerSize(DeepLearningConstants.LAYER_SIZE)
                    .learningRate(DeepLearningConstants.LEARNING_RATE)
                    //.labelsSource(source)
                    .batchSize(DeepLearningConstants.BATCH_SIZE)
                    .labels(Constants.labelList)
                    .windowSize(DeepLearningConstants.WINDOW_SIZE)
                    .iterate(iterator)
                    .trainWordVectors(DeepLearningConstants.TRAIN_WORD_VECTORS)
                    .vocabCache(cache)
                    .tokenizerFactory(t)
                    .sampling(DeepLearningConstants.SAMPLING)
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
                    .minWordFrequency(DeepLearningConstants.MIN_WORD_FREQUENCY)
                    .iterations(DeepLearningConstants.ITERATION_COUNT)
                    .epochs(DeepLearningConstants.EPOCHS)
                    .layerSize(DeepLearningConstants.LAYER_SIZE)
                    .learningRate(DeepLearningConstants.LEARNING_RATE)
                    .windowSize(DeepLearningConstants.WINDOW_SIZE)
                    .batchSize(DeepLearningConstants.BATCH_SIZE)
                    .iterate(iterator)
                    .vocabCache(cache)
                    .tokenizerFactory(t)
                    .sampling(DeepLearningConstants.SAMPLING)
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
        KMeansClustering kmc = KMeansClustering.setup(DeepLearningConstants.KMEANS_CLUSTER_COUNT, DeepLearningConstants.KMEANS_ITERATION_COUNT,
                DeepLearningConstants.KMEANS_DISTANCE_FUNCTION);
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







    public SequenceVectors sequenceVectorModel(String filePath) throws Exception
    {
        filePath = "raw_sentences.txt";
        File file = new File(filePath);

        AbstractCache<VocabWord> vocabCache = new AbstractCache.Builder<VocabWord>().build();

        //First we build line iterator
        BasicLineIterator underlyingIterator = new BasicLineIterator(file);

        /*
            Now we need the way to convert lines into Sequences of VocabWords.
            In this example that's SentenceTransformer
         */
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();
        tokenizer.setTokenPreProcessor(new CommonPreprocessor());

        SentenceTransformer transformer = new SentenceTransformer.Builder()
                .iterator(underlyingIterator)
                .tokenizerFactory(tokenizer)
                .build();

        //And we pack that transformer into AbstractSequenceIterator
        AbstractSequenceIterator<VocabWord> sequenceIterator = new AbstractSequenceIterator.Builder<>(transformer).build();

        /*
            Now we should build vocabulary out of sequence iterator.
            We can skip this phase, and just set AbstractVectors.resetModel(TRUE), and vocabulary will be mastered internally
        */
        VocabConstructor<VocabWord> constructor = new VocabConstructor.Builder<VocabWord>()
                .addSource(sequenceIterator, DeepLearningConstants.MIN_WORD_FREQUENCY)      // Default : 5
                .setTargetVocabCache(vocabCache)
                .build();

        constructor.buildJointVocabulary(false, true);

        //Time to build WeightLookupTable instance for our new model
        WeightLookupTable<VocabWord> lookupTable = new InMemoryLookupTable.Builder<VocabWord>()
                .vectorLength(DeepLearningConstants.LAYER_SIZE)     // Default : 150
                .useAdaGrad(false)
                .cache(vocabCache)
                .build();

         /*
             reset model is viable only if you're setting AbstractVectors.resetModel() to false
             if set to True - it will be called internally
        */
        lookupTable.resetWeights(true);

        //Now we can build AbstractVectors model, that suits our needs
        SequenceVectors<VocabWord> vectors = new SequenceVectors.Builder<VocabWord>(new VectorsConfiguration())
                // minimum number of occurrences for each element in training corpus. All elements below this value will be ignored
                // Please note: this value has effect only if resetModel() set to TRUE, for internal model building. Otherwise it'll be ignored, and actual vocabulary content will be used
                .minWordFrequency(DeepLearningConstants.MIN_WORD_FREQUENCY)      // Default : 5

                // WeightLookupTable
                .lookupTable(lookupTable)

                // abstract iterator that covers training corpus
                .iterate(sequenceIterator)

                // vocabulary built prior to modelling
                .vocabCache(vocabCache)

                // batchSize is the number of sequences being processed by 1 thread at once
                // this value actually matters if you have iterations > 1
                .batchSize(DeepLearningConstants.BATCH_SIZE)      // Default : 250

                // number of iterations over batch
                .iterations(DeepLearningConstants.ITERATION_COUNT)      // Default : 1

                // number of iterations over whole training corpus
                .epochs(DeepLearningConstants.EPOCHS)      // Default : 1

                // if set to true, vocabulary will be built from scratches internally
                // otherwise externally provided vocab will be used
                .resetModel(false)

                //These two methods define our training goals. At least one goal should be set to TRUE.
                .trainElementsRepresentation(true)
                .trainSequencesRepresentation(false)

                //Specifies elements learning algorithms. SkipGram, for example.
                .elementsLearningAlgorithm(new SkipGram<VocabWord>())

                .build();

        //Now, after all options are set, we just call fit()
        vectors.fit();

        /*
            As soon as fit() exits, model considered built, and we can test it.
            Please note: all similarity context is handled via SequenceElement's labels, so if you're using AbstractVectors to build models for complex
            objects/relations please take care of Labels uniqueness and meaning for yourself.
         */
        //double sim = vectors.similarity("day", "night");
        //log.info("Day/night similarity: " + sim);

        return vectors;
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
        WordVectorSerializer.writeParagraphVectors(vec, filePath);
    }
}
