/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Dylan Danse
 */
public class PrintedWord{
    private String word;
    private Color color;
    private Font font;

    public PrintedWord(String word, Color color, Font font) {
        this.word = word;
        this.color = color;
        this.font = font;
    }
    
    public PrintedWord(WeighedWord word, String font){
        this.word = word.getWord();        
        int freq = word.getFrequency();
        this.font = new Font(font, freq*10);
        
        if (freq <= 2) {
            this.color = Color.GREEN;
        }else if(freq <= 4){
            this.color = Color.ORANGE;
        }else {
            this.color = Color.RED;
        }
    }
    
    public PrintedWord(WeighedWord word){
        this.word = word.getWord();        
        int freq = word.getFrequency();
        this.font = new Font("System", freq*7);
        
        if (freq <= 2) {
            this.color = Color.GREEN;
        }else if(freq <= 4){
            this.color = Color.ORANGE;
        }else {
            this.color = Color.RED;
        }
    }
    
    public String getWord() {
        return word;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Font getFont() {
        return font;
    }
}
