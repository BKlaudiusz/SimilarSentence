package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;

/**
 * Wlasny wyjatek jezeli uzytkownik podal zle haslo
 */
public class WrongPassowrd extends Exception {

    public WrongPassowrd(String message) {
        super(message);
    }
}
