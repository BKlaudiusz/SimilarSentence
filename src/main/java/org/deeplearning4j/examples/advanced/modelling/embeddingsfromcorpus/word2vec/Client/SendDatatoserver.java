package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.Client;

import org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Klasa do wysylania wiadomosci do serwera
 */
public class SendDatatoserver  extends Thread {
    ObjectOutputStream objectOutputStream ;
    final CommunicatiowithServer reciveDataFromServer;
    Socket socket;

    SendDatatoserver(ObjectOutputStream a, CommunicatiowithServer b, Socket socket)
    {
        this.socket = socket;
        objectOutputStream  = a;
        reciveDataFromServer = b;
    }

    /**
     * Metoda do komunikacji z serwerem
     */
    public void run()
    {
        boolean endcomunication = false;
        Scanner scanner = new Scanner(System.in);
        displaymenu();
        while (!socket.isClosed()) {

            if (scanner.hasNext()) {
                String input = scanner.nextLine();
                int command=Integer.parseInt(input);
                switch (command)
                {
                    case 1:
                        System.out.println("Log in\nInput nickname");
                        String nicknamelog = scanner.nextLine();
                        System.out.println("NickName" + nicknamelog);
                        System.out.println("Input password");
                        String passwordlog = scanner.nextLine();
                        User userlog = new User(nicknamelog,passwordlog);
                        try {
                            objectOutputStream.writeInt(DataFromUser.LOGIN.ordinal());
                            objectOutputStream.writeObject(userlog);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        System.out.println("Register\nInput nickname");
                        String nickname = scanner.nextLine();
                        System.out.println("NickName" + nickname);
                        System.out.println("Input password");
                        String password = scanner.nextLine();
                        User user = new User(nickname,password);
                        try {
                            objectOutputStream.writeInt(DataFromUser.REGISTER.ordinal());
                            objectOutputStream.writeObject(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        System.out.println("Forgot password\nInput nickname");
                        input = scanner.nextLine();
                        System.out.println("NickName" + input);
                        try {
                            objectOutputStream.writeInt(DataFromUser.RESET_PASSWORD.ordinal());
                            objectOutputStream.writeObject(input);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        System.out.println("Log out");
                        endcomunication = true;
                        break;
                    case 5:
                        System.out.println("Input sentence");
                        input = scanner.nextLine();
                        try {
                            objectOutputStream.writeInt(DataFromUser.GET_SENTENCE.ordinal());
                            objectOutputStream.writeObject(input);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                if(endcomunication)
                    break;

                    try {
                        objectOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private void displaymenu()
    {
        if(!reciveDataFromServer.chceckisLogin())
        {
            System.out.println("\n\n1 - log in\n2 - register\n3 - forgot password\n");
        }else
        {
            System.out.println("4 - log out\n2 - Input sentence\n");
        }
    }
}
