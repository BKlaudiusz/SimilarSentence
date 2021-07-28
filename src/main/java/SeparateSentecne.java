import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SeparateSentecne {
    static String[] SENTENCE;
    public static void main(String[] args) throws IOException {
        Scanner sentence = new Scanner(new File
            ("C:\\Users\\Hp omen\\Dl4j\\deeplearning4j-examples\\dl4j-examples\\The-Communist-Manifesto.txt"));
        ArrayList<String> sentenceList = new ArrayList<String>();

        while (sentence.hasNextLine())
        {
            sentenceList.add(sentence.nextLine());
        }

        sentence.close();

        String[] sentenceArray = sentenceList.toArray(new String[sentenceList.size()]);
        FileWriter f = new FileWriter("filename.txt",true);
        PrintWriter out = new PrintWriter(f);
        for (int r=0;r<sentenceArray.length;r++)
        {
            SENTENCE = sentenceArray[r].split("(?<=[.!?])\\s*");
            for (int i=0;i<SENTENCE.length;i++)
            {
                if(SENTENCE[i].length()>5) {
                    SENTENCE[i] = SENTENCE[i].replaceAll(","," , ");
                    SENTENCE[i] = SENTENCE[i].replaceAll("[.]"," .");
                    System.out.println("Sentence " + (i + 1) + ": " + SENTENCE[i]);
                    out.println(SENTENCE[i]);
                }
            }
        }
        out.close();
    }
}
