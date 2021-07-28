package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;

import java.io.FileNotFoundException;

public class main {
    public static void main(String[] args) {

        try {
            AnalyzerSentence analyzerSentence = new AnalyzerSentence();
            System.out.println(analyzerSentence.getBestFitSentence("They have bad memory"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
