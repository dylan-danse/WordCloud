/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Dylan
 */
public class TextParser {
    
     public static List<WeighedWord> stringToWeighedWords(String text, int nbMots, int tailleMin, int freqMin) {
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
        words.removeIf(p -> p.getFrequency() < freqMin);
        return words;
    }
     
     public static String textFileToString(String path){
        StringBuilder sb = new StringBuilder();
        String line;
        int octetsRead;
        byte[]tampon;
        
        try(InputStream out = Files.newInputStream(Paths.get(path), StandardOpenOption.READ)) {            
            tampon = new byte[256];
            octetsRead = out.read(tampon);
            while (octetsRead > 0) {
                line = new String(tampon, StandardCharsets.ISO_8859_1);
                sb.append(line);
                octetsRead = out.read(tampon);
            }
        } catch (IOException ex) {
            System.out.println("Specified path is not accessible");
            ex.printStackTrace(System.err);
        }        
        return sb.toString();
    }
}
