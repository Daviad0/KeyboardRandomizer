/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daviado
 */
public class KeyboardPhase {
    public Task currentTask;
    public String name;
    public KeyboardSetup currentSetup;
    public double requiredScore;
    public int currentTaskIndex = 0;
    public double currentScore = 0;
    public int numCompleted;
    public ArrayList<Task> allTasks = new ArrayList<Task>();
    public String[] allPossibleWords;
    
    private void getWords() throws FileNotFoundException{
        //System.out.println(new File(".").getAbsolutePath());
        Scanner inFile = new Scanner(new File("./src/main/java/com/daviadoprojects/keyboardrandomizer/words.txt"));
        StringBuilder sb = new StringBuilder();
        while(inFile.hasNext()){
            sb.append(inFile.next());
            sb.append("\n");
        }
        inFile.close();
        System.out.println(sb.toString());
        allPossibleWords = sb.toString().split("\n");
        
    }
    
    public void initializeWalkthrough(){
        allTasks.add(new Task(1, "Find and type the vowel given", "a", new String[]{"a"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "e", new String[]{"e"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "i", new String[]{"i"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "o", new String[]{"o"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "u", new String[]{"u"}, true));
        allTasks.add(new Task(2, "Type out everything given", "aeiou", new String[]{"a", "e", "i", "o", "u"}, true));
        allTasks.add(new Task(2, "Type out everything given", "uoiea", new String[]{"a", "e", "i", "o", "u"}, true));
        
 
        int numberOfPractices = (int)(Math.random()*30)+40;
        for(int i = 0; i < numberOfPractices; i++){
            int randomLetterIndex = (int)(Math.random()*KeyboardSetup.keyString.length());
            String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex + 1);
            allTasks.add(new Task(1, "Give me a letter!", randomLetter, new String[]{randomLetter}, true));
        }
       
        
    }
    
    public void addRandom(int number){
        for(int i = 0; i < number; i++){
            int randomChoice = (int)(Math.random()*10);
            String toBeCorrect = "";
            if(randomChoice <= 1){
                int letters = (int)(Math.random()*9)+2;
                for(int l = 0; l < letters; l++){
                    int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                    String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                    toBeCorrect = toBeCorrect + randomLetter;
                }
            }else if(randomChoice <= 5){
                int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                toBeCorrect = randomLetter;
            }else{
                int randomWordIndex = (int)(Math.random() * allPossibleWords.length);
                toBeCorrect = allPossibleWords[randomWordIndex];
            }
            
            allTasks.add(new Task((int)(toBeCorrect.length()/2)+1, "Please type in the given word", toBeCorrect, new String[]{}, false));
            
        }
    }
    
    public KeyboardPhase(String name, KeyboardSetup setup, double requiredScore){
        this.name = name;
        this.currentSetup = setup;
        this.requiredScore = requiredScore;
        try {
            getWords();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        initializeWalkthrough();
        addRandom(50);
        currentTask = allTasks.get(0);
        
    }
    
    public void completeTask(){
        currentTaskIndex++;
        currentScore += currentTask.score;
        if(currentTaskIndex >= allTasks.size()){
            addRandom(10);
        }
        numCompleted++;
        currentTask = allTasks.get(currentTaskIndex);
    }
}
