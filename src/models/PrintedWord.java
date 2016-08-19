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
    
    public PrintedWord(WeighedWord word, String font, Colors color){
        this.word = word.getWord();        
        int freq = word.getFrequency();
        this.font = new Font(font, freq*8);
        
        switch(color){
            case Blue:
                if (freq <= 2)
                    this.color = Color.CORNFLOWERBLUE;
                else if(freq <= 4)
                    this.color = Color.BLUE;
                else
                    this.color = Color.DARKBLUE;
                break;
            case Green:
                if (freq <= 2)
                    this.color = Color.LIGHTGREEN;
                else if(freq <= 4)
                    this.color = Color.GREEN;
                else
                    this.color = Color.DARKGREEN;
                break;
            case Red:
                if (freq <= 2)
                    this.color = Color.SALMON;
                else if(freq <= 4)
                    this.color = Color.RED;
                else
                    this.color = Color.DARKRED;
                break;
                
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