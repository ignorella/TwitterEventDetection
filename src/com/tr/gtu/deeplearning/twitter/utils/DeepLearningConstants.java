package com.tr.gtu.deeplearning.twitter.utils;

/**
 * Created by TugbaYAGIZ on 24.05.2018.
 */
public class DeepLearningConstants
{
    /*
     * Deep Learning Model Constants
     */
    public static double LEARNING_RATE = 0.025;
    public static int ITERATION_COUNT = 100;
    public static int LAYER_SIZE = 100;
    public static int EPOCHS = 10;
    public static int WINDOW_SIZE = 5;
    public static int MIN_WORD_FREQUENCY = 5;
    public static int SAMPLING = 0;
    public static int BATCH_SIZE = 1000;
    public static boolean TRAIN_WORD_VECTORS = true;

    /*
     * K-Means Clustering - Constants
     */
    public static int KMEANS_ITERATION_COUNT = 30;
    public static int KMEANS_CLUSTER_COUNT = 30;
    public static String KMEANS_DISTANCE_FUNCTION = "cosinesimilarity";
}
