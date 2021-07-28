package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Client;

import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.ListSentence;
import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Sentence;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * Klasa do obierania widomosci od serwera
 */
public class CommunicatiowithServer extends Thread implements Runnable{
    private final String path;
     final String myhostName;
     Socket echoSocket;
     private InputStream inputStream;
     private OutputStream outputStream;
     private ObjectInputStream objectInputStream;
     private SendDatatoserver sendDatatoserver;
     private ObjectOutputStream objectOutputStream;
     private boolean isLogin;
     private int portNumber;
    CommunicatiowithServer(String hostName, int portNumber) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        path = "src/main/java/org/deeplearning4j/examples/advanced/modelling/embeddingsfromcorpus/word2vec/";
        isLogin = false;
        myhostName = hostName;
        this.portNumber = portNumber;
        clientSocketInitialization();
        //echoSocket = new Socket(hostName, portNumber);

        inputStream = echoSocket.getInputStream();
        outputStream = echoSocket.getOutputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        objectOutputStream = new ObjectOutputStream(outputStream);
        sendDatatoserver = new SendDatatoserver(objectOutputStream,this,echoSocket);
        sendDatatoserver.start();
    }

    public synchronized boolean chceckisLogin()
    {
            return isLogin;
    }
    public synchronized void loginSuccess()
    {
        isLogin = true;
    }

    /**
     * Wysietlanie odpowiedzi od serwera
     */
    @Override
    public void run()
    {

        System.out.println("Connected!");
        System.out.print(echoSocket);

        while (!echoSocket.isClosed())
        {
                try {

                    int respond = objectInputStream.readInt();

                    System.out.println(DataFromUser.fromInt(respond));
                    switch (DataFromUser.fromInt(respond))
                    {
                        case LOGIN_SUCCESS:
                            System.out.println("Login Success");
                            this.loginSuccess();
                            break;
                        case USER_DONT_EXIST:
                            System.out.println("User don't exist!");
                            break;
                        case RESET_PASSWORD:
                            String respondpassowrd = (String)objectInputStream.readObject();
                            System.out.println("Your passowrd: " + respondpassowrd);
                            break;
                        case WRONG_PASSWORD:
                            System.out.println("Wrong password!");
                            break;
                        case USER_EXIST:
                            System.out.println("User with this nickname exist!");
                            break;
                        case REGISTER_SUCCESS:
                            System.out.println("Register success,log in!");
                            break;
                        case GET_SENTENCE:
                            ListSentence result = (ListSentence)objectInputStream.readObject();
                            System.out.println(result);

                    }
                    displaymenu();

                } catch (IOException | ClassNotFoundException e ) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
        }
        System.out.print("KONIEC");
    }

    /**
     * Inicjalizacja Szyfrowanej komunikacji
     */
    private void clientSocketInitialization() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(path + "key.jks"), "SearchSentences".toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "SearchSentences".toCharArray());
        KeyManager[] km = keyManagerFactory.getKeyManagers();

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        TrustManager[] tm = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(km, tm, null);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        echoSocket = sslSocketFactory.createSocket("localhost", portNumber);
    }

    /**
     * Metoda do wysietlania menu
     */
    private void displaymenu()
    {
        if(!chceckisLogin())
        {
            System.out.println("\n\n1 - log in\n2 - register\n3 - forgot password\n");
        }else
        {
            System.out.println("4 - log out\n5 - Input sentence\n");
        }
    }
}
