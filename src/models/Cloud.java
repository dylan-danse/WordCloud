/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Dylan
 */
public class Cloud extends ArrayList<PrintedWord> {
    
    public Cloud(){}
    
    public Cloud(Collection<WeighedWord> words, String fontFamily, Colors color){        
        words.stream().forEach((weighedWord) -> {
            this.add(new PrintedWord(weighedWord, fontFamily, color));
        });
    }
}
