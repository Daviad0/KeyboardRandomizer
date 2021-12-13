/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

/**
 *
 * @author daviado
 */
public class Task {
    public double score;
    public String instructions;
    public String correctAnswer;
    public String[] lightUp;
    public boolean isPractice;
    public Task(double score, String instructions, String correctAnswer, String[] lightUp, boolean isPractice){
        this.score = score;
        this.instructions = instructions;
        this.correctAnswer = correctAnswer;
        this.lightUp = lightUp;
        this.isPractice = isPractice;
    }
    
    
    
}
