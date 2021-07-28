import com.google.common.base.Splitter;
import org.bytedeco.opencv.presets.opencv_core;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class test {

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Hp omen\\Dl4j\\deeplearning4j-examples\\dl4j-examples\\src\\main\\java\\raw_sentence.txt");
        System.out.println(file.exists());
        String filePath = "C:\\Users\\Hp omen\\Dl4j\\deeplearning4j-examples\\dl4j-examples\\src\\main\\java\\raw_sentence.txt";
        System.out.println("Load & Vectorize Sentences....");
// Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec = new Word2Vec.Builder()
            .minWordFrequency(5)
            .layerSize(100)
            .seed(48)
            .windowSize(5)
            .iterate(iter)
            .tokenizerFactory(t)
            .build();


        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        WordVectorSerializer.writeWordVectors(vec, "pathToWriteto.txt");

      //  Word2Vec vec = WordVectorSerializer.readWord2VecModel("pathToWriteto.txt");
        /*
        System.out.println("Closest Words:");
        Collection<String> lst = vec.wordsNearest("safe", 15);
        System.out.println(lst);
        double cosSim = vec.similarity("safe", "money");
        System.out.println("work money ==    " +cosSim);
        demoString1 = "increased speed";
        demoString2 = "Improve manufacture";
        System.out.println("Similarity Score between: "+demoString1+" --vs-- "+ demoString2 +":==>"+ cosineSimForSentence(vec, demoString1, demoString2));
*/

// TODO
//POCZYTAC O PARAMETRACH
// OBSLUGA PLIKU XML
// POLACZNIE Z KLIENTEM
        List<String> sentenceList = new ArrayList<>();
        Scanner sentence = new Scanner(new File("C:\\Users\\Hp omen\\Dl4j\\deeplearning4j-examples\\dl4j-examples\\src\\main\\java\\raw_sentence.txt"));
        while (sentence.hasNextLine())
        {
            sentenceList.add(sentence.nextLine());
        }
        SentecneStat[] sentecneStats = new SentecneStat[10];
        for (int i = 0; i < sentecneStats.length; i++) {
            sentecneStats[i] = new SentecneStat();
        }
        for ( String element : sentenceList)
        {
            double result = cosineSimForSentence(vec, "this is the best time", element);
            for(SentecneStat senc:sentecneStats)
            {
                if(senc.getRate()<result)
                {
                    senc.setRate(result);
                    senc.setValue(element);
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(sentecneStats));
    }

    public static double cosineSimForSentence(Word2Vec vector, String sentence1, String sentence2){
        Collection<String> label1 = Splitter.on(' ').splitToList(sentence1);
        Collection<String> label2 = Splitter.on(' ').splitToList(sentence2);
        try{
            return Transforms.cosineSim(vector.getWordVectorsMean(label1), vector.getWordVectorsMean(label2));
        }catch(Exception e){
            return 0;
            //exceptionMessage = e.getMessage();
        }
     //   return Transforms.cosineSim(vector.getWordVectorsMean(label1), vector.getWordVectorsMean(label2));
    }
}
