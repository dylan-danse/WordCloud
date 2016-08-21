/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Dylan
 */
public class TextParser {
    
     public static List<WeighedWord> stringToWeighedWords(String text, int nbWords, int minSize, int minFreq, boolean isOrdered) {
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
                if(currentWord.length() >= minSize){
                    word = new WeighedWord(currentWord, 1);
                    if(words.contains(word)){
                        words.get(words.indexOf(word)).incrementFrequency();
                    }else{
                        words.add(word);
                    }
                }                                    
            }
        }
        words.removeIf(p -> p.getFrequency() < minFreq);
        
        //TODO : Choose way to truncate collection
        if(words.size() > nbWords)
            words = words.subList(0, nbWords);
        
        if(isOrdered)
            Collections.sort(words);
        else
            Collections.shuffle(words);
        
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
     
     public static String getExtension(File file){
        String extension = "";
        String fileName = file.getAbsolutePath();
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
     }
}
