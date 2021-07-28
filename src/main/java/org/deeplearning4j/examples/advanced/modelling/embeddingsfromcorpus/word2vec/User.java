package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;

import java.io.Serializable;

/**
 * Klasa do reprezentacji uzytkownika
 */
public class User implements Serializable {
    private String nickName;
    private String password;

    /**
     *
     * @param nickName nazwa uzytkownika
     * @param password haslo
     */
    public User(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }

    /**
     *
     * @return nazwa uzytkownika
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @return haslo
     */
    public String getPassword() {
        return password;
    }
}
