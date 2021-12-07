/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

import java.util.ArrayList;

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
    public ArrayList<Task> allTasks = new ArrayList<Task>();
    
    public void initializeWalkthrough(){
        allTasks.add(new Task(0, "Find and type the letter 'a'", "a", new String[]{"a"}));
        allTasks.add(new Task(0, "Find and type the letter 'e'", "e", new String[]{"e"}));
        allTasks.add(new Task(0, "Find and type the letter 'i'", "i", new String[]{"i"}));
        allTasks.add(new Task(0, "Find and type the letter 'o'", "o", new String[]{"o"}));
        allTasks.add(new Task(0, "Find and type the letter 'u'", "u", new String[]{"u"}));
        allTasks.add(new Task(0, "Type out all 'aeiou'", "aeiou", new String[]{"a", "e", "i", "o", "u"}));
        allTasks.add(new Task(0, "Now type out all 'uoiea'", "uoiea", new String[]{"a", "e", "i", "o", "u"}));
        
        allTasks.add(new Task(0, "Give me a 's'!", "s", new String[]{"s"}));
        allTasks.add(new Task(0, "Give me a 'd'!", "d", new String[]{"d"}));
        allTasks.add(new Task(0, "Give me a 'f'!", "f", new String[]{"f"}));
        
    }
    
    public void addRandom(int number){
        for(int i = 0; i < number; i++){
            boolean isPhrase = (int)(Math.random()*2) == 1;
            String toBeCorrect = "";
            if(isPhrase){
                int letters = (int)(Math.random()*10);
                for(int l = 0; l < letters; l++){
                    int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                    String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                    toBeCorrect = toBeCorrect + randomLetter;
                }
            }else{
                int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                toBeCorrect = randomLetter;
            }
            
            allTasks.add(new Task(1, "Please type in '" + toBeCorrect +"'", toBeCorrect, new String[]{}));
            
        }
    }
    
    public KeyboardPhase(String name, KeyboardSetup setup, double requiredScore){
        this.name = name;
        this.currentSetup = setup;
        this.requiredScore = requiredScore;
        initializeWalkthrough();
        addRandom(50);
        currentTask = allTasks.get(0);
    }
    
    public void completeTask(){
        currentTaskIndex++;
        if(currentTaskIndex >= allTasks.size()){
            addRandom(10);
        }
        
        currentTask = allTasks.get(currentTaskIndex);
    }
}
