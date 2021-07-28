package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Serwer;

import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.*;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;

/**
 * Enum do komunikacji z uwytkownikem
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

public class EchoSeverThreadForOnceClient extends  Thread
{
    private final SSLSocket socket;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    AnalyzerSentence analyzerSentence;
    WaitFormClient server;

    /**
     *
     * @param socket Socket
     * @param c watek ktory czeka na klientow
     * @param analyzerSentence Klasa do analizy zdan
     * @throws IOException Problem z komukacja brak socketa
     */
    EchoSeverThreadForOnceClient(SSLSocket socket, WaitFormClient c, AnalyzerSentence analyzerSentence) throws IOException {
        this.socket = socket;
        this.analyzerSentence = analyzerSentence;
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
        objectInputStream = new ObjectInputStream(inputStream);
        server = c;

        //socket.startHandshake();
       // SSLSession sslSession = socket.getSession();

    }

    /**
     * Metoda do odopowiadania uzytkownikowi
     */
    @Override
    public void run()
    {
        while (!socket.isClosed())
        {
            try {

                int userInput = objectInputStream.readInt();
                System.out.print("User ,"+socket +"napisal " +DataFromUser.fromInt(userInput)+"\n");
                System.out.println();
                if(DataFromUser.fromInt(userInput) != null)
                {
                    switch (DataFromUser.fromInt(userInput))
                    {
                        case LOGIN:
                            User userLogin = (User)objectInputStream.readObject();
                            System.out.println(userLogin.getPassword()+"             "+userLogin.getNickName());

                            try {
                                if(server.findUser(userLogin))
                                {
                                    objectOutputStream.writeInt(DataFromUser.LOGIN_SUCCESS.ordinal());
                                }else
                                {
                                    objectOutputStream.writeInt(DataFromUser.USER_DONT_EXIST.ordinal());
                                }
                            } catch (WrongPassowrd wrongPassowrd) {
                                objectOutputStream.writeInt(DataFromUser.WRONG_PASSWORD.ordinal());
                                break;
                            }
                            break;
                        case REGISTER:
                            User register = (User)objectInputStream.readObject();
                            try {
                                if(server.registerUser(register))
                                {
                                    objectOutputStream.writeInt(DataFromUser.REGISTER_SUCCESS.ordinal());
                                }else
                                {
                                    objectOutputStream.writeInt(DataFromUser.USER_EXIST.ordinal());
                                }
                                break;
                            }catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (TransformerException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            }
                        case RESET_PASSWORD:
                            String usernickname =(String)objectInputStream.readObject();
                            String respond = server.resetPassowrd(usernickname);
                            if(respond.length()== 0)
                            {
                                objectOutputStream.writeInt(DataFromUser.USER_DONT_EXIST.ordinal());
                            }else
                            {
                                objectOutputStream.writeInt(DataFromUser.RESET_PASSWORD.ordinal());
                                objectOutputStream.writeObject(respond);
                            }
                            break;
                        case GET_SENTENCE:
                            String sentence =(String)objectInputStream.readObject();
                            ListSentence result= analyzerSentence.getBestFitSentence(sentence);
                            objectOutputStream.writeInt(DataFromUser.GET_SENTENCE.ordinal());
                            objectOutputStream.writeObject(result);
                            System.out.println("AAAA");
                            break;
                    }
                }
                objectOutputStream.flush();
                objectOutputStream.reset();
            } catch (IOException | ClassNotFoundException e) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
}
