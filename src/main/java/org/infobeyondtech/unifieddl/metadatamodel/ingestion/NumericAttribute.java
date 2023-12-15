package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

public class NumericAttribute extends Attribute{

    float min;
    float max;
    float mean;
    float mode;
    float median;
    int positiveCount;
    int negativeCount;
    boolean isUniform;

    float kurtosis;
    float skewness;
    // standard deviation
    float standardDeviation;

    float variance;
    float lowerOuterFence;
    float higherOuterFence;
    float lowerQuartile;
    float higherQuartile;
    float higherConfidence;
    float lowerConfidence;
}
