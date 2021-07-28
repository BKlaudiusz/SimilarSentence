package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Serwer;

import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.AnalyzerSentence;


public class Serwer {

    public static void main(String[] args) {
        WaitFormClient waitFormClient = null;
        try {
            AnalyzerSentence analyzerSentence =new AnalyzerSentence();
            waitFormClient = new WaitFormClient(9998,analyzerSentence);
            waitFormClient.waitForClient();
        } catch (Exception e) {
            System.out.println("Problem z uruchomieniem serwera...");
        }

    }
}
