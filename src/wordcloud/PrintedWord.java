/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcloud;

import javafx.scene.paint.Color;

/**
 *
 * @author Dylan Danse
 */
public class PrintedWord {
    private String word;
    private int size;    
    private Color color;

    public PrintedWord(String word, int size, Color color) {
        this.word = word;
        this.size = size;
        this.color = color;
    }
    
    public String getWord() {
        return word;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }
}