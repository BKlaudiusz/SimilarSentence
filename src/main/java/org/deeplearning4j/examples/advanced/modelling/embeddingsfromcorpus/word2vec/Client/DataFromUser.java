package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Client;

/**
 * Enum do komunikacji z uzytkownikiem
 */
enum DataFromUser {
    LOGIN,
    REGISTER,
    RESET_PASSWORD,
    WRONG_PASSWORD,
    USER_DONT_EXIST,
    GET_SENTENCE,
    LOGIN_SUCCESS,
    USER_EXIST,
    REGISTER_SUCCESS;

    public static DataFromUser fromInt(int x) {
        switch(x) {
            case 0:
                return LOGIN;
            case 1:
                return REGISTER;
            case 2:
                return RESET_PASSWORD;
            case 3:
                return WRONG_PASSWORD;
            case 4:
                return USER_DONT_EXIST;
            case 5:
                return GET_SENTENCE;
            case 6:
                return LOGIN_SUCCESS;
            case 7:
                return USER_EXIST;
            case 8:
                return REGISTER_SUCCESS;
        }
        return null;
    }
}
