/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daviadoprojects.keyboardrandomizer;

// add required imports, mostly readying for file import
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
    // define instance variables for when a new phase is defined
    // keeps track of the current task at hand for the user to complete
    public Task currentTask;
    public String name;
    // keeps track of the current keyboard that is being used
    public KeyboardSetup currentSetup;
    // required score may be different based on the phase
    public double requiredScore;
    public int currentTaskIndex = 0;
    public double currentScore = 0;
    public int numCompleted;
    // this will keep track of all of the tasks that have to be done (all completed tasks are still in here)
    public ArrayList<Task> allTasks = new ArrayList<Task>();
    public String[] allPossibleWords;
    
    private void getWords() throws FileNotFoundException{
        // get path of the words file and put it in a scanner
        Scanner inFile = new Scanner(new File("./src/main/java/com/daviadoprojects/keyboardrandomizer/words.txt"));
        StringBuilder sb = new StringBuilder();
        while(inFile.hasNext()){
            // build each line of the string and add a line break
            sb.append(inFile.next());
            sb.append("\n");
        }
        // close and split the string into an array
        inFile.close();
        allPossibleWords = sb.toString().split("\n");
        
    }
    
    // to be run only once in the constructor
    public void initializeWalkthrough(){
        // initialize very BASIC tasks with the keys highlighted
        allTasks.add(new Task(1, "Find and type the vowel given", "a", new String[]{"a"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "e", new String[]{"e"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "i", new String[]{"i"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "o", new String[]{"o"}, true));
        allTasks.add(new Task(1, "Find and type the vowel given", "u", new String[]{"u"}, true));
        allTasks.add(new Task(2, "Type out everything given", "aeiou", new String[]{"a", "e", "i", "o", "u"}, true));
        allTasks.add(new Task(2, "Type out everything given", "uoiea", new String[]{"a", "e", "i", "o", "u"}, true));
        
        // add more single character practices with the letters highlighted to the all tasks list
        int numberOfPractices = (int)(Math.random()*30)+40;
        for(int i = 0; i < numberOfPractices; i++){
            int randomLetterIndex = (int)(Math.random()*KeyboardSetup.keyString.length());
            String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex + 1);
            allTasks.add(new Task(1, "Give me a letter!", randomLetter, new String[]{randomLetter}, true));
        }
       
        
    }
    
    // adds a random number of randomly generated items to the tasks
    public void addRandom(int number){
        // loops through number amount of times to get all the items requested
        for(int i = 0; i < number; i++){
            // gets a random # 0-9 (inc)
            int randomChoice = (int)(Math.random()*10);
            String toBeCorrect = "";
            // checks if program should make a randomly generated spam of characters (10%)
            if(randomChoice <= 1){
                int letters = (int)(Math.random()*9)+2;
                // loop thru how many letters we want
                for(int l = 0; l < letters; l++){
                    int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                    String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                    toBeCorrect = toBeCorrect + randomLetter;
                }
            }
            // checks if program should make a randomly generated character WITHOUT highlighting (40%)
            else if(randomChoice <= 5){
                int randomLetterIndex = (int)(Math.random() * KeyboardSetup.keyString.length());
                String randomLetter = KeyboardSetup.keyString.substring(randomLetterIndex, randomLetterIndex+1);
                toBeCorrect = randomLetter;
            }
            // checks if should generate a real word (50%)
            else{
                int randomWordIndex = (int)(Math.random() * allPossibleWords.length);
                toBeCorrect = allPossibleWords[randomWordIndex];
            }
            // add task based on what the final content was without highlighting
            allTasks.add(new Task((int)(toBeCorrect.length()/2)+1, "Please type in the given word", toBeCorrect, new String[]{}, false));
            
        }
    }
    // one constructor, no overload
    public KeyboardPhase(String name, KeyboardSetup setup, double requiredScore){
        // give variables from this instance values 
        this.name = name;
        this.currentSetup = setup;
        this.requiredScore = requiredScore;
        // try to get all the words, but be able to catch an exception as needed
        try {
            getWords();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        // initialize first 100 or so tasks
        initializeWalkthrough();
        addRandom(50);
        // start out on task 1
        currentTask = allTasks.get(0);
        
    }
    
    public void completeTask(){
        // increment current task index
        currentTaskIndex++;
        currentScore += currentTask.score;
        // check if more tasks are needed in the list (finishing code is in the MainRandomizer class)
        if(currentTaskIndex >= allTasks.size()){
            addRandom(10);
        }
        numCompleted++;
        // get new task
        currentTask = allTasks.get(currentTaskIndex);
    }
}
