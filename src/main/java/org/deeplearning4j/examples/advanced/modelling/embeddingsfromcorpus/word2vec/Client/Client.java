package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Client;

import java.io.*;

public class Client {
    public static void main(String[] args) {
        CommunicatiowithServer communicatiowithServer = null;
        try {
            communicatiowithServer = new CommunicatiowithServer("localhost",9998);
            communicatiowithServer.start();
        } catch (Exception e) {
            System.out.println("Problem z komunikacja z serwerem");
        }

    }
}
