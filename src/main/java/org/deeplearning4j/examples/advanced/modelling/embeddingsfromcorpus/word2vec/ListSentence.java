package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa do wysylania podobnych zdan uzytkownikowi
 */
public class ListSentence implements Serializable {
    private final ArrayList<Sentence> arrayListSentence;


    ListSentence()
    {
        arrayListSentence = new ArrayList<>();
    }
    public void addSentence(Sentence sentence)
    {
        arrayListSentence.add(sentence);
    }

    @Override
    public String toString() {
        return "ListSentence{" +
            "arrayListSentence=" + arrayListSentence +
            '}';
    }
}
