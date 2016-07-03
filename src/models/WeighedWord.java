/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Dylan
 */
public class WeighedWord{
    private String word;
    private int frequency;

    public WeighedWord(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public void incrementFrequency(){
        this.frequency++;
    }

    

    @Override
    public boolean equals(Object o) {
        return this.word.equalsIgnoreCase(((WeighedWord)o).getWord());
    }

    @Override
    public String toString() {
        return this.word + " - " + this.frequency;
    }
}
