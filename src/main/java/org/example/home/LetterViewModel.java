package org.example.home;


public class LetterViewModel {

    private String letter;
    private int index;
    private String colour;

    public LetterViewModel(String letter, int index, String colour) {
        this.letter = letter;
        this.index = index;
        this.colour = colour;
    }

    public String getLetter() {
        return letter;
    }

    public int getIndex() {
        return index;
    }

    public String getColour() {
        return colour;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
