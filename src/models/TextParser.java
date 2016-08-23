/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        BreakIterator iterator = BreakIterator.getWordInstance();
        iterator.setText(text);
        
        int last = iterator.first();
        int first;
        
        while (BreakIterator.DONE != last) {
            first = last;
            last = iterator.next();

            if (Character.isLetterOrDigit(text.charAt(first)) && last != BreakIterator.DONE) 
            {
                currentWord = text.substring(first, last).toLowerCase();
                if(currentWord.length() >= minSize){
                    word = new WeighedWord(currentWord, 1);
                    if(words.contains(word))
                        words.get(words.indexOf(word)).incrementFrequency();
                    else
                        words.add(word);
                    
                }                                    
            }
        }
        words.removeIf(p -> p.getFrequency() < minFreq);
        
        words.sort(Comparator.comparing(WeighedWord::getFrequency));
        if(words.size() > nbWords)
            words = words.subList(words.size()-nbWords, words.size());
        
        if(isOrdered)
            Collections.sort(words);
        else
            Collections.shuffle(words);
        
        return words;
    }
     
    public static String textFileToString(String path){
        StringBuilder sb = new StringBuilder();
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),StandardCharsets.ISO_8859_1))){
            
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            reader.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
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
