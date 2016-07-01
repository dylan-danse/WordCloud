/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcloud;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dylan
 */
public class TextParser {
    
     public static List<WeighedWord> getWords(String text, int nbMots, int tailleMin) {
        List<WeighedWord> words = new ArrayList<>();
        String currentWord;
        WeighedWord word;

        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();

            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) 
            {
                currentWord = text.substring(firstIndex, lastIndex).toLowerCase();
                if(currentWord.length() >= tailleMin){
                    word = new WeighedWord(currentWord, 1);
                    if(words.contains(word)){
                        words.get(words.indexOf(word)).incrementFrequency();
                    }else{
                        words.add(word);
                    }
                }                                    
            }
        }        
        return words;
    }
}
