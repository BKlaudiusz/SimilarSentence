package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;
import org.deeplearning4j.models.word2vec.Word2Vec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Book {

    private  List<Sentence> sentenceList;
    private final String NameBook ;
    private final Word2Vec vec;

    Book(String path, String namefile, Word2Vec vec) throws IOException {
        sentenceList = new ArrayList<>();
        NameBook = namefile;
        this.vec = vec;
        exportSentence(path+namefile);
    }

    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    public String getNameBook() {
        return NameBook;
    }
    private void exportSentence(String path) throws FileNotFoundException {
        String[] SENTENCE;
        Scanner sentence = new Scanner(new File(path));
        ArrayList<String> sentenceList = new ArrayList<>();

        while (sentence.hasNextLine()) {
            sentenceList.add(sentence.nextLine());
        }

        sentence.close();

        String[] sentenceArray = sentenceList.toArray(new String[sentenceList.size()]);

        int iteratorToLine = 1;

        String temp="";
        for (String s : sentenceArray) {
            SENTENCE = s.split("(?<=[.!?])\\s*");
            if(SENTENCE.length == 1)
            {
                temp = SENTENCE[0];
            }else {
                for (int i = 0; i < SENTENCE.length; i++) {
                    if (SENTENCE[i].length() > 10) {
                        if(temp.length()!=0)
                        {
                            SENTENCE[i] = temp +SENTENCE[i];
                            temp="";
                        }
                        if(i == SENTENCE.length -1)
                        {
                            temp = SENTENCE[i];
                        }
                        String a = SENTENCE[i].replaceAll("[0-9*#@$%\\[\\]']", " ");
                        a = a.replaceAll("\\s[*]\\s]", ".");
                        a = a.trim().replaceAll("\\s{2,}", " ");

                        try
                        {
                            Sentence sentenceToAdd = new Sentence(a, vec,iteratorToLine,this.getNameBook());
                            this.sentenceList.add(sentenceToAdd);
                        }catch (IllegalStateException ignored){}
                        iteratorToLine++;
                    }
                }
            }
        }
    }
}
