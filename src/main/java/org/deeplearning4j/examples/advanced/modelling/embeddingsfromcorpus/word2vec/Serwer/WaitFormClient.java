package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Serwer;


import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.AnalyzerSentence;
import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.User;
import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.WrongPassowrd;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

/**
 * Watek do czekania na klientow
 */
public class WaitFormClient extends Thread{
    private final int portNumber;
    ServerSocket serverSocket;
    private ServerSocket sslServerSocket;
    AnalyzerSentence analyzerSentence;
    private ArrayList<EchoSeverThreadForOnceClient> ClietOnline;
    private NodeList listUser;
    private  String absolutePath;

    /**
     *
     * @param Portnumber port
     * @param analyzerSentence model do porowanina zdan
     * @throws IOException Brak plikow
     * @throws ParserConfigurationException Problem z odczytanie XML
     * @throws SAXException Problem z odczytanie XML
     * @throws UnrecoverableKeyException Problem z komunikacja SSL
     * @throws CertificateException  Problem z komunikacja SSL - certyfikat
     * @throws NoSuchAlgorithmException Problem z komunikacja SSL
     * @throws KeyStoreException Problem z komunikacja SSL
     * @throws KeyManagementException Problem z komunikacja SSL
     */
    WaitFormClient(int Portnumber, AnalyzerSentence analyzerSentence) throws IOException, ParserConfigurationException, SAXException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        absolutePath = "src/main/java/org/deeplearning4j/examples/advanced/modelling/embeddingsfromcorpus/word2vec/";
        this.portNumber =Portnumber;
        this.analyzerSentence = analyzerSentence;
       // serverSocket =  new ServerSocket(portNumber);
        ClietOnline = new ArrayList<>();
        serverSocketInitialization();
        loadUser();
    }

    /**
     * MEtoda na czekanie na klienta
     */
    public void waitForClient()
    {
        try{
            Socket clientSocket;
            SSLSocket sslSocket;
            System.out.println("ServerSocket awaiting connections...");
            while ((sslSocket = (SSLSocket) sslServerSocket.accept())!=null)
            {
                System.out.println("Connection from " + sslSocket + "!");
                EchoSeverThreadForOnceClient ClientThread = new EchoSeverThreadForOnceClient(sslSocket,this,analyzerSentence);
                ClientThread.start();
                ClietOnline.add(ClientThread);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inicjalizacja Komunikacja szyfrowane SSL
     */
    private void serverSocketInitialization() throws IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException, KeyStoreException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(absolutePath + "key.jks"), "SearchSentences".toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "SearchSentences".toCharArray());
        KeyManager[] km = keyManagerFactory.getKeyManagers();

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        TrustManager[] tm = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(km, tm, null);

        ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        sslServerSocket = serverSocketFactory.createServerSocket(portNumber);
    }

    /**
     * Uzytkownik zakonczyl polaczenie z serwerem
     * @param e watek dla uzytkownika
     */
    public void removeuser(EchoSeverThreadForOnceClient e)
    {
        ClietOnline.remove(e);
    }

    /**
     * Wszytanie pliku z uzytkownikami
     * @throws ParserConfigurationException Problem z sparsowanie pliku
     * @throws IOException Brak pliku
     * @throws SAXException
     */
    private void loadUser() throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File("src/main/java/org/deeplearning4j/examples/advanced/modelling/embeddingsfromcorpus/word2vec/Serwer/User.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        listUser = doc.getElementsByTagName("user");
    }

    /**
     * Sprawdzenie czy uzytkownik isntnieje
     * @param user uzytkownik
     * @return true jak istnieje false jak nie
     * @throws WrongPassowrd uzytkownik istnieje ale z innym haslem
     */
    public boolean findUser(User user) throws WrongPassowrd
    {
        for (int temp = 0; temp < listUser.getLength(); temp++) {

            Node nNode = listUser.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if(eElement.getElementsByTagName("nickname").item(0).getTextContent().equals(user.getNickName()))
                {
                    if(eElement.getElementsByTagName("password").item(0).getTextContent().equals(user.getPassword()))
                        return true;
                    else
                    {
                        throw new WrongPassowrd(
                            "Wrong password  with nick " + user.getNickName());
                    }
                }
            }
        }
        return false;
    }

    /**
     * Rejestracja uzytkownka
     * @param user  uzytkownik
     * @return true jak sie udalo  false jak nie
     */
    public boolean registerUser(User user) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        if(!userIsExist(user))
        {
            String filepath = "src/main/java/org/deeplearning4j/examples/advanced/modelling/embeddingsfromcorpus/word2vec/Serwer/User.xml";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node company = doc.getFirstChild();

            // append a new node to staff
            Element newPerson = doc.createElement("user");
            company.appendChild(newPerson);


            Element firstName = doc.createElement("nickname");
            firstName.setTextContent(user.getNickName());

            Element lastName = doc.createElement("password");
            lastName.setTextContent(user.getPassword());

            newPerson.appendChild(firstName);
            newPerson.appendChild(lastName);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
            loadUser();
            return  true;
        }
        return false;
    }

    public boolean userIsExist(User user)
    {

        for (int temp = 0; temp < listUser.getLength(); temp++) {

            Node nNode = listUser.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println( eElement.getElementsByTagName("nickname").item(0).getTextContent());
                System.out.println( eElement.getElementsByTagName("password").item(0).getTextContent());

                if(eElement.getElementsByTagName("nickname").item(0).getTextContent().equals(user.getNickName()))
                {
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Zwraca haslo uzytkownika o danym nicku
     * @param nickname nazwa uzytkownika
     * @return haslo lub pusty String
     */
    public String resetPassowrd(String nickname)
    {
        for (int temp = 0; temp < listUser.getLength(); temp++) {

            Node nNode = listUser.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if(eElement.getElementsByTagName("nickname").item(0).getTextContent().equals(nickname))
                {
                    return eElement.getElementsByTagName("password").item(0).getTextContent();
                }
            }
        }
        return "";
    }
}
