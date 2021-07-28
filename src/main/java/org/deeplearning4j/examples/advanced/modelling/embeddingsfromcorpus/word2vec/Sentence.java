package org.deeplearning4j.examples.advanced.modelling.embeddingsfromcorpus.word2vec;

import com.google.common.base.Splitter;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.Serializable;
import java.util.*;

public class Sentence implements Serializable {
    private final String sentence;
    private INDArray indArray;
    private  int line;
    private  String title;
    private  double probability;

    /**
     *
     * @param sentence zdanie do przetworzenia
     * @param word2Vec siec neoroniwa
     */
    Sentence(String sentence,Word2Vec word2Vec)
    {
        this.sentence = sentence;
        probability = 0;
        Collection<String> label =  Splitter.on(' ').splitToList(sentence);
        try
        {
            indArray = word2Vec.getWordVectorsMean(label);
        }catch (IllegalStateException ignored){}
    }
    Sentence(String sentence,String title,double probability,int line)
    {
        this.sentence = sentence;
        this.title = title;
        this.probability = probability;
        this.line = line;
    }
    Sentence(String s, Word2Vec vector,int l,String title)
    {
        probability = 0;
        this.title = title;
        sentence = s;
        line = l;
        indArray  = Nd4j.zeros(vector.getLayerSize(), vector.getLayerSize());
        Collection<String> label =  Splitter.on(' ').splitToList(s);
        indArray = vector.getWordVectorsMean(label);
    }

    /**
     *
     * @param probability metoda do ustawienia prawodopobienstwa
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     *
     * @return String do wyswietlenia
     */
    @Override
    public String toString() {
        return "----------------------------------------\n" +
            "sentence='" + sentence + '\n' +
            ", line=" + line +"\n"+
            ", title='" + title  +"\n"+
            ", Podobienstwo ='" + probability  +"\n"
            +"----------------------------------------\n" ;
    }

    /**
     *
     * @return zwraca zdanie
     */
    public String getSentence() {
        return sentence;
    }

    /**
     *
     * @return zwraca wektor zdania
     */
    public INDArray getIndArray() {
        return indArray;
    }

    /**
     *
     * @return linnia w pliku gdzie znajduje sie zdanie
     */
    public int getLine() {
        return line;
    }

    /**
     *
     * @return tytul
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return pobobienstwo
     */
    public double getProbability() {
        return probability;
    }

    /**
     *
     * @param sentence1 zdanie do porownania
     * @return podobienstwo
     */
    public Double compareTo(Sentence sentence1) {
        return Transforms.cosineSim(indArray, sentence1.getIndArray());
    }
}
